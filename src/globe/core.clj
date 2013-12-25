(ns globe.core
  (:require
   [clojure.core.async :refer [chan]]
   [clojure.core.typed :refer :all]))

;Types!!


(def-alias World-Map Map)

(def-alias World '{:event-queue  Chan
                   :world-map    World-Map
                   :tic          AnyInteger})

(def-alias World-Path  '[Symbol Symbol])
(def-alias Point       '[AnyInteger AnyInteger])
(def-alias Location    '[World-Path Point])

(def-alias Spot (U World-Path
                   Location))

(def-alias Square (List Obj))
(def-alias Grid (Vec (Vec Square)))

(def-alias Obj Any)
(def-alias Tic AnyInteger)

(def-alias Node-Params '{:obj Obj :location Location})
(def-alias Add-Node '[:add-node Node-Params])
(def-alias Remove-Node '[:remove-node Node-Params])

(def-alias Action
  (U Add-Node
     Remove-Node))

(def-alias Event  '{:sender Obj :action Action :tic Tic})


(ann loc-0 Location)
(def loc-0 [:awesome-world :level-1 0 0])

(ann  world [Chan Map AnyInteger -> World])
(defn world [ch m x] {:event-queue ch :world-map m :tic x})

(ann empty-world World)
(def empty-world (world chan
                  {:awesome-world {:level-1 [['(1 7) '(2 3)]
                                             []
                                             []
                                             []]}}
                  0))


(ann  get-spot [World Spot -> (Option (U Square Grid))])
(defn get-spot
  "Givein a location get-location navigates the world
  to try to find the place asked for, it will return a
  list of things found"
  [world spot]
  (get-in (:world-map world) spot))

(ann update-spot [World Spot (U Square Grid) -> (Option (U Grid World-Map))])

(defn update-spot
  [world spot o]
  (update-in (:world-map world) spot #(o)))

(ann  assoc-spot [World Spot (U Square Grid) -> (Option (U Grid World-Map))])
(defn assoc-spot
  [world loc o]
  (assoc-in (:world-map world) loc o))

(ann tic [World -> World])
(defn tic [world]
  (assoc world :tic (inc (:tic world))))

(defn loc-wp
  [[w l _ _]] (vec [w l]))


(def tag first)
(def body rest)
(ann run-event [World Event -> World])
(defmulti run-event (fn [_ event] tag (:action event)))

(defmethod run-event :add-node [world {[_ action] :action}]
  (let [loc (:location action)
        square (get-spot world loc)
        obj    (:obj action)
        updated-square (conj square obj)]
  (assoc-spot world loc updated-square)))

(defmethod run-event :remove-node [world {[_ action] :action}]
  (let [loc (:location action)
        square (get-spot world loc)
        obj    (:obj action)
        updated-square (remove (partial = obj) square )]
  (assoc-spot world loc updated-square)))

(ann test-event Event)
(def test-event {:sender 0 :action [:remove-node {:obj 1 :location loc-0}] :tic 0 })

(run-event empty-world test-event)

(get-location empty-world loc-0)









