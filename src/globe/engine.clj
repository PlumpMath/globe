(ns globe.engine
   (:require
   [clojure.entities :refer :all]
   [clojure.core.async :refer [chan]]
   [clojure.core.typed :refer :all]))


(def-alias World-Map Map)
(def-alias World-State '{:base-objects Map
                         :world-objects Map})


(def-alias World '{:events  Chan
                   :map    World-Map
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

(def-alias Add-Node    '[:add-node    '{:location Location :ref Keyword}])
(def-alias Remove-Node '[:remove-node '{:obj    Obj :location Location}])
(def-alias Damage      '[:damage      '{:sender Obj :target Obj :amount AnyInteger}])
(def-alias Move        '[:move        '{:target Obj :location Location}])
(def-alias Action
  (U Add-Node
     Remove-Node
     Damage
     Move))

(def-alias Event  '{:sender Obj :action Action :tic Tic})



(ann loc-0 Location)
(def loc-0 [:awesome-world :level-1 0 0])

(ann  world [Chan Map AnyInteger -> World])
(defn world [ch m x m2 m3] {:events ch :map m :tic x :base-objects m2 :world-objects m3})



(ann empty-world World)
(def empty-world (world chan
                  {:awesome-world {:level-1 [['(1 7) '(2 3)]
                                             []
                                             []
                                             []]}}
                  0 {} {}))




(ann tic [World -> World])
(defn tic [world]
  (assoc world :tic (inc (:tic world))))

(defn loc-wp
  [[w l _ _]] (vec [w l]))


(ann run-event [World Event -> World])
(defmulti run-event (fn [_ {[tag & _] :action}] tag))

(defmethod run-event :add-node [world {[_ {:keys [location obj]}] :action}]
  (add-node location obj))

(defmethod run-event :remove-node [world {[_ {:keys [location obj]}] :action}]
  (remove-node location obj))

(defmethod run-event :damage [world {[_ {:keys [sender target amount]}] :action}]
  (let [new-hp (- (get-in target [:stats :hp]) amount)]
    (if (< amount 0)
    (remove-node (get-in target [:stats :location]) target)
    )))




