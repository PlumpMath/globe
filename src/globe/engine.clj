(ns globe.engine
   (:require
   [globe.events :refer :all]
   [clojure.core.async :refer [chan go >!!]]
   [clojure.core.typed :refer :all]))

(def-alias EventChan (Chan (Event)))
(ann  start-simple-world [World -> EventChan])
(defn start-simple-world [world]
  (let [c (chan)]
    (go (while true
          (let [v (<!! c)]
            (run-event v c)))) c))
