(ns globe.engine
   (:require
   [globe.entities :refer :all]
   [clojure.core.async :refer [chan]]
   [clojure.core.typed :refer :all]))


(def-alias World-Map Map)
(def-alias World-State '{:base-objects Map
                         :world-objects Map})


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

(def-alias Obj Any)
(def-alias Tic AnyInteger)
(def-alias InstId Keyword)
(def-alias Add-Node    '[:add-node    '{:id InstId :location Location}])
(def-alias Remove-Node '[:remove-node '{:id InstId :location Location}])
(def-alias Add-Base    '[:add-base    '{:obj Obj}])
(def-alias Remove-Base '[:remove-base '{:obj Obj}])
(def-alias Create      '[:create      '{:id InstId :location Location}])
(def-alias Destroy     '[:destroy     '{:id InstId :location Location}])
(def-alias Damage      '[:damage      '{:target InstId :amount AnyInteger}])
(def-alias Heal        '[:heal        '{:target InstId :amount AnyInteger}])
(def-alias Move        '[:move        '{:id InstId :location Location :old Location}])
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

(def-alias Event  '{:sender Obj :action Action :tic Tic})


(ann  tic [World -> World])
(defn tic [world]
  (assoc world :tic (inc (:tic world))))

(ann  loc-wp [Location -> WorldPoint])
(defn loc-wp
  [[w l _ _]] (vec [w l]))

(ann run-event [World Event -> World])
(defmulti run-event (fn [_ {[tag & _] :action}] tag))

(defmethod run-event :add-node [world {[_ {:keys [location id]}] :action}]
  (add-node location id))

(defmethod run-event :remove-node [world {[_ {:keys [location id]}] :action}]
  (remove-node location id))

(defmethod run-event :damage  [world {[_ {:keys [sender-id target-id amount]}] :action}]
  (let [new-hp (- (get-obj-stat target-id :hp) amount)]
    (if (< amount 0)
     (destroy-obj world target-id)
     (assoc-obj world target-id :hp new-hp))))

(defmethod run-event :heal [world {[_ {:keys [sender-id target-id amount]}] :action}]
 )
(defmethod run-event :create  [world {[_ {:keys [id location]}] :action}]
  (init-obj world id location))

(defmethod run-event :destroy [world {[_ {:keys [id]}] :action}]
  (destroy-obj world id))

(defmethod run-event :move    [world {[_ {:keys [id location]}] :action}]
  (move-obj world id location))

(defmethod run-event :tic [world]
  (tic world))

(defmethod run-event :die []
  (comment "die here... I dont know how"))
