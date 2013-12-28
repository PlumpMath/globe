(ns globe.engine
   (:require
   [globe.events :refer :all]
   [clojure.core.incubator :refer :all]
   [clojure.core.async :refer [chan go >! <! mult filter> tap]]
   [clojure.pprint :refer :all]
   [clojure.core.typed :as t :refer :all]))

(def-alias EventChan (Chan (Event)))

(defn do-event [w e c]
  (pprint e)
  (when-let [clean-e (check-event w e c)]
                (or (run-event w clean-e c) w)))


(ann  start-simple-world [World -> EventChan])
(defn start-world [world]
      (let [c (chan 500)]
        (go (loop [w world]
            (pprint w)
                (recur (or (do-event w (<! c) c) w))))
        c))
