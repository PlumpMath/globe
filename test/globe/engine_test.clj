(ns globe.engine-test
  (:require
   [clojure.test :refer :all]
   [midje.sweet :refer :all]
   [globe.engine :as engine]))


(fact "midpoint gets the midpoint" 
  (engine/midpoint [0 0] [4 4]) => [2 2] 
  (engine/midpoint [3 0] [0 4]) => [1 2])

(fact "ceil should ceil"
  (engine/ceil 10 4) => 4
  (engine/ceil 4 10) => 4)

(fact "random square generates a square in
      positive space with in the bounds and
      smaller then the max size")

(fact "directions"
 (engine/top [[0 0] [1 1]]) =>    [[0 1] [1 1]]
 (engine/bottom [[0 0] [1 1]]) => [[0 0] [1 0]]
 (engine/left [[0 0] [1 1]]) =>   [[1 0] [1 1]]
 (engine/right [[0 0] [1 1]]) =>  [[1 0] [1 1]])


