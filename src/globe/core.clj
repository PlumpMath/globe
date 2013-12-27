(ns globe.core
  (:require
   [globe.entities :refer :all]
   [globe.engine   :refer :all]))

empty-world

(create-base empty-world (thing [(visable "*")]
                       {:name "wood floor" :sym :wood-floor :hp 100}))




(def first-world
  (-> empty-world
    (add-base {:ables #{:visable}
                  :stats  {:name "wood floor"
                           :char '*
                           :prefix "wdf"
                           :sym :wood-floor
                           :hp 100}} )
    (add-base {:ables #{:visable :collideable}
                  :stats  {:name "stone wall"
                           :char 'x
                           :prefix "stw"
                           :sym :stone-wall
                           :hp 1000}})
    (add-base {:ables #{:visable :collideable}
                  :stats  {:name "stone statue"
                          :char 's
                          :prefix "sts"
                          :sym :stone-statue
                          :hp 50}})

))






(ann test-event Event)
(def test-event {:sender 0 :action [:add-node {:obj 2 :locatoin loc-0}]})

(testthing empty-world test-event)
(get-spot empty-world loc-0)








