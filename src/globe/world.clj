(ns globe.world
  (:require
   [clojure.core.incubator :refer :all]
   [clojure.core.async :refer [go >! >!!]]
   [clojure.core.typed :refer :all]))

(def-alias World-Map Map)
(def-alias World-State '{:base-objects Map
                         :world-objects Map
                         :living-objects Map})


(def-alias World '{:map    World-Map
                   :state  World-State
                   :tic          AnyInteger})


(def-alias World-Path  '[Keyword Keyword])
(def-alias Point       '[AnyInteger AnyInteger])
(def-alias Location    '[World-Path Point])

(def-alias Spot (U World-Path
                   Location))

(def-alias Square (List Obj))
(def-alias Grid (Vec (Vec Square)))

(def-alias LifeFunc (Fn [World Id EventChan -> (Vec Events)]))

(defn add-living [world id life-func c]
   (assoc-in world [:state :living-objects :id] {:life-func life-func :ch c}))

(ann  run-one [World Id '{:life-func LifeFunc :ch EventChan} -> (Vec Events)])
(defn run-one [world id {:keys [life-func ch]}]
  (life-func world id ch))

(ann  add-base [World Obj -> World])
(defn add-base [world obj]
  (assoc-in world  ([:state :base-objects (get-in obj [:stats :sym])]) obj))

;This is going to have bug
;dissocing a base object this is in use will probably break things...

(ann  remove-base [World Obj ->World])
(defn remove-base [world obj]
  (dissoc-in world ([:state :base-objects (get-in obj [:stats :sym])]) obj))

(ann  get-spot [World Spot -> (Option (U Square Grid))])
(defn get-spot [world spot]
  (get-in world [:map spot] ))

(ann  update-spot [World Spot (Spot -> Spot) -> (Option World)])
(defn update-spot [world spot f]
  (update-in  world [:map spot] f))

(ann  assoc-spot [World Spot (U Square Grid) -> (Option World)])
(defn assoc-spot [world loc o]
  (assoc-in world [:map loc] o))


(ann  add-node [World Spot Keyword -> (Option World)])
(defn add-node [world loc id]
  (let [square (get-spot world loc)
        updated-square (conj square id)]
  (assoc-spot world loc updated-square)))

(ann  remove-node [World Spot Keyword -> (Option World)])
(defn remove-node [world loc id]
  (let [square (get-spot world loc)
        updated-square (disj square id)]
  (assoc-spot world loc updated-square)))

(ann  move-node [World Spot Spot Keyword -> (Option World)])
(defn move-node [world old-loc new-loc id]
  (-> (remove-node old-loc id)
      (add-node    new-loc id)))

(ann  genname [String -> Keyword])
(defn genname [base]
  (keyword (gensym (get-in base [:states :prefix]))))

(ann  get-obj [World Keyword -> Obj])
(defn get-obj [world id]
  (get-in [:state :world-objects id]))


(ann  init-obj [World Keyword Spot -> World])
(defn init-obj [{:keys [state tic] :as world}  base loc]
  (let [base-obj (get-in state [:base-objects base])
        loc-obj (assoc-in base-obj [:stats :location] loc)
        id (genname loc-obj)
        inst-obj (assoc-in loc-obj [:stats :id] id)]
    (-> assoc-in world [:state :world-objects id] inst-obj
    (add-node loc inst-obj))))

(ann  update-obj [World Keyword Keyword Fn])
(defn update-obj [world id k f]
  (update-in world [:state :world-objects id k] f))

(ann  assoc-obj [World Keyword Keyword Any])
(defn assoc-obj [world id k v]
  (assoc-in world [:state :world-objects id k] v))

(ann  move-obj [World Spot Spot Keyword -> (Option World)])
(defn move-obj [world new-loc id]
  (let [old-loc (:location (get-obj world id))]
  (-> (move-node world old-loc new-loc id)
      (assoc-obj id :location new-loc))))

(ann  destroy-obj [World InstId -> World])
(defn destroy-obj [world id]
  (let [loc (:location (get-obj world id))]
  (-> (dissoc-in world [:state :world-objects id])
      (remove-node loc id))))

(ann  get-obj-stat [World InstId Keyword -> Any])
(defn get-obj-stat [world id k]
  (get-in [:state :world-objects id :stats k]))

(ann  update-obj-stat [World InstId Keyword Fn -> World])
(defn update-obj-stat [world id k f]
  (update-in [:state :world-objects id :stats k] f))

(ann  assoc-obj-stat [World InstId Keyword Fn -> World])
(defn assoc-obj-stat [world id k v]
  (assoc-in [:state :world-objects id :stats k] v))


(ann  tic [World -> World])
(defn tic [world]
  (assoc world :tic (inc (:tic world))))


