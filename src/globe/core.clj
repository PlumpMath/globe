(ns globe.core
  (:require
   [clojure.core.async :refer [chan]]
   [clojure.core.typed :refer :all]))

(ann-record World [event-queue :- Chan
                   world-map   :- Map
                   tic         :- AnyInteger])
(defrecord World [event-queue world-map tic])

(ann-record Location [world :- Symbol
                      level :- Symbol
                      x     :- AnyInteger
                      y     :- AnyInteger])
(defrecord Location [world level x y])

(ann loc-0 Location)
(def loc-0 (Location. ':world ':1 0 0))


(def-alias Action
  (U (I ':add-node Any Location)
     (I ':remove-node Any Location)))


(ann-record Event [doer      :- Any
                   action    :- Action
                   tic       :- Anyinteger])
(defrecord Event [doer action tic])


(cf ('add-node 1 loc-0) Action)


(ann empty-world World)
(def empty-world
  (World. (chan) {} 0))

(ann  get-location [World Location -> (Option (Vec Any))])
(defn get-location
  "Givein a location get-location navigates the world
  to try to find the place asked for, it will return a
  list of things found"
  [world {:keys [world level x y]}]
  (world (level (get x (get y world)))))

(ann tic [World -> World])
(defn tic [world]
  (assoc world :tic (inc (:tic world))))

(ann run-event [World Event -> World])
(defmulti run-event (fn [world event] (:action event)))

(defmethod run-event :add-node [world {:keys [doer action paramaters tic]}]
  (get-location world (:location paramaters)))





