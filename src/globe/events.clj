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

(ann       run-event [World Event EventQueue -> World])
(defmulti  run-event (fn [world [sender [tag params] tic] queue] tag))

(defmethod run-event :add-node    [world [_ [_ [location id]] _] _]
  (add-node    world location id))

(defmethod run-event :remove-node [world [_ [_ [location id]] _] _]
  (remove-node world location id))

(defmethod run-event :add-base    [world [_ [_ [obj]] _] _]
  (add-base world obj))

(defmethod run-event :remove-base  [world [_ [_ [obj]] _] _]
  (remove-base world obj))

(defmethod run-event :damage   [world [sender [_ [target-id amount]] _] _]
  (let [new-hp (- (get-obj-stat target-id :hp) amount)]
    (if (< amount 0)
     (destroy-obj world target-id)
     (assoc-obj world target-id :hp new-hp))))

(defmethod run-event :damage   [world [sender [_ [target-id amount]] _] _]
     (update-obj world target-id :hp (partial + amount)))

(defmethod run-event :create  [world {[_ {:keys [id location]}] :action}]
  (init-obj world id location))

(defmethod run-event :destroy [world {[_ {:keys [id]}] :action} _]
  (destroy-obj world id))

(defmethod run-event :move    [world {[_ {:keys [id location]}] :action} _]
  (move-obj world id location))

(defmethod run-event :tic [world _ _]
  (tic world))

(defmethod run-event :die [_ _ _]
  (comment "die here... I dont know how"))

