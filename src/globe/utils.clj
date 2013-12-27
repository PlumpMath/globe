(ns globe.utils
  (:require
   [globe.engine :refer :all]
   [globe.entities :refer :all]))


(ann loc-0 Location)
(def loc-0 [:awesome-world :level-1 0 0])

(ann  world [Chan Map AnyInteger -> World])
(defn world [ch m x m2 m3] {:events ch :map m :tic x :base-objects m2 :world-objects m3})

(ann empty-world World)
(def empty-world (world chan
                  {:awesome-world {:level-1 [[#{:player :wall} #{:wall}]
                                             []
                                             []
                                             []]}}
                  0 {} {}))

