(ns globe.events
 (:require
  [clojure.core.typed :refer :all]
  [clojure.core.async  :refer [go mult filter> >! chan tap map> onto-chan]]
  [globe.world :as world :refer :all]))

(def-alias Obj Any)
(def-alias Tic AnyInteger)
(def-alias InstId Keyword)
(def-alias Add-Node    '[:add-node    '[InstId Location]])
(def-alias Remove-Node '[:remove-node '[InstId Location]])
(def-alias Add-Base    '[:add-base    '[Obj]])
(def-alias Remove-Base '[:remove-base '[Obj]])
(def-alias Create      '[:create      '[InstId Location]])
(def-alias Destroy     '[:destroy     '[InstId Location]])
(def-alias Damage      '[:damage      '[InstId AnyInteger]])
(def-alias Heal        '[:heal        '[InstId AnyInteger]])
(def-alias Move        '[:move        '[InstId Location Location]])
(def-alias Tic         '[:tic])
(def-alias Die         '[:die])

;Probably should have a checkpoint event
(def-alias Action
  (U Add-Node
     Remove-Node
     Add-Base
     Remove-Base
     Create
     Destroy
     Heal
     Damage
     Move
     Tic
     Die))

(defn e-tic [[inst a tic]] tic)

(def-alias Event  '[InstId Action Tic])

;should be changed to a case statement to deal with old events
(defn check-event [world event ch]
  (if (> (e-tic event)(:tic world))
    (do (go (do (Thread/sleep 100) (>! ch event))) nil)
    (if (< (e-tic event)(:tic world)) nil event)))

(ann       run-event [World Event EventChan -> World])
(defmulti  run-event (fn [world [sender [tag params] tic] ch] tag))

(defmethod run-event :add-node
  [world [_ [_ [location id]] _] _]
  (world/add-node    world location id))

(defmethod run-event :remove-node
  [world [_ [_ [location id]] _] _]
  (world/remove-node world location id))

(defmethod run-event :add-base
  [world [_ [_ [obj]] _] _]
  (world/add-base world obj))

(defmethod run-event :remove-base
  [world [_ [_ [obj]] _] _]
  (world/remove-base world obj))

(defmethod run-event :damage
  [world [sender [_ [target-id amount]] _] _]
  (let [new-hp (- (get-obj-stat target-id :hp) amount)]
    (if (< amount 0)
     (world/destroy-obj world target-id)
     (world/assoc-obj world target-id :hp new-hp))))

(defmethod run-event :damage
  [world [sender [_ [target-id amount]] _] _]
  (world/update-obj world target-id :hp (partial + amount)))

(defmethod run-event :create
  [world [sender [_ [id location]] _] _]
  (world/init-obj world id location))

(defmethod run-event :destroy
  [world [sender [_ [id location]] _] _]
  (world/destroy-obj world id))

(defmethod run-event :move
  [world [sender [_ [id location]] _] _]
  (world/move-obj world id location))

(defmethod run-event :tic
  [world [_ [_ [forwarded]]] ch]
  (do
    (if (not (empty? forwarded))
    (onto-chan ch forwarded))
    (world/tic world)))

(defmethod run-event :die
  [_ _ _]
  (comment "die here... I dont know how"))

(defmethod run-event :life
  [world _ ch]
  (let [living (get-in world [:state :living-objects])]
    (map> (fn [k v] (world/run-one world k v)) ch)))

(defmethod run-event :enliven
  [world [sender [_ [id life-func filter-fun] tic] ch]]
  (let [c (chan 500)]
   (world/add-living world id life-func (tap (mult ch) (filter> filter-fun c)))))

(defmethod run-event :kill
  [world [ _ [_ [id] _] _]]
    (world/remove-living world id))
