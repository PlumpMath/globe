(ns globe.world-test
  (:require
   [[clojure.test :refer :all]
   [midje.sweet :refer :all]
   [globe.world :as world]]))

(fact "add node should a object to the world tree"
(world/add-node 
  world/empty-world [:world :level1 0 0] :player) =>      
    {:map {:world {:level1 {0 {0 (:player)}}}}, 
       :state {:base-objects {}, 
               :world-objects {}, 
               :living-objects {}}, 
               :tic 0})


