(ns globe.entities
  (:require
   [clojure.core.async :refer [go >! >!!]]
   [clojure.core.typed :refer :all]))

(defn add-base [world obj]
  (assoc-in world  ([:base-objects (get-in obj [:stats :sym])]) obj))

(ann god Keyword)
(def god (keyword (gensym "god")))

(ann  get-spot [World Spot -> (Option (U Square Grid))])
(defn get-spot
  "Givein a location get-location navigates the world
  to try to find the place asked for, it will return a
  list of things found"
  [world spot]
  (get-in (:map world) spot))

(ann update-spot [World Spot (U Square Grid) -> (Option (U Grid World-Map))])
(defn update-spot
  [world spot o]
  (update-in (:map world) spot #(o)))

(ann  assoc-spot [World Spot (U Square Grid) -> (Option (U Grid World-Map))])
(defn assoc-spot
  [world loc o]
  (assoc-in (:map world) loc o))

(defn add-node [world loc obj]
  (let [square (get-spot world loc)
        updated-square (conj square obj)]
  (assoc-spot world loc updated-square)))

(defn remove-node [world loc obj]
  (let [square (get-spot world loc)
        updated-square (remove (partial = obj) square )]
  (assoc-spot world loc updated-square)))
(ann  init-obj [World Symbol Location -> Instance])

(defn update-node [world loc obj]
  (let [square (get-spot world loc)
        new-square (remove (partial = obj) square)
        updated-square (conj new-square obj)]
  (assoc-spot world loc updated-square)))

(defn genname [base]
  (keyword (gensym (get-in base [:states :prefix]))))

(defn init-obj [{:keys [events state tic]} base loc]
  (let [base-obj (get-in state [:base-objects base])
        loc-obj (assoc-in base-obj [:stats :location] loc)
        id (genname loc-obj)
        init-obj (assoc-in loc-obj [:stats :id] id)]
    (conj (:world-objects state) inst-obj )
    (>! events {:sender god :action '[:add-node '{:location loc :ref id }] :tic tic})))








