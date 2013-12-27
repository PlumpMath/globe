(ns globe.events
 (:require
  [clojure.core.typed :refer :all]
  [globe.world :refer :all]))

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

(def-alias Event  '[InstId Action Tic])

(ann       run-event [World Event EventMult -> World])
(defmulti  run-event (fn [world [sender [tag params] tic] mul] tag))

(defmethod run-event :add-node
  [world [_ [_ [location id]] _] _]
  (add-node    world location id))

(defmethod run-event :remove-node
  [world [_ [_ [location id]] _] _]
  (remove-node world location id))

(defmethod run-event :add-base
  [world [_ [_ [obj]] _] _]
  (add-base world obj))

(defmethod run-event :remove-base
  [world [_ [_ [obj]] _] _]
  (remove-base world obj))

(defmethod run-event :damage
  [world [sender [_ [target-id amount]] _] _]
  (let [new-hp (- (get-obj-stat target-id :hp) amount)]
    (if (< amount 0)
     (destroy-obj world target-id)
     (assoc-obj world target-id :hp new-hp))))

(defmethod run-event :damage
  [world [sender [_ [target-id amount]] _] _]
  (update-obj world target-id :hp (partial + amount)))

(defmethod run-event :create
  [world [sender [_ [id location]] _] _]
  (init-obj world id location))

(defmethod run-event :destroy
  [world [sender [_ [id location]] _] _]
  (destroy-obj world id))

(defmethod run-event :move
  [world [sender [_ [id location]] _] _]
  (move-obj world id location))

(defmethod run-event :tic
  [world _ _]
  (tic world))

(defmethod run-event :die
  [_ _ _]
  (comment "die here... I dont know how"))

(defmethod run-event :enliven
  [world [sender [_ [id life-func filter-fun] tic] mul]]
  (let [c (chan)]
   (add-living world id life-func (tap mul (filter> filter-fun c)))))

(defmethod run-event :kill
  [world [ _ [_ [id] _] _]]
    (remove-living world id))
