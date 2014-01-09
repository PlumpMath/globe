(ns globe.engine
   (:require
   [globe.events :refer :all]
   [clojure.core.incubator :refer :all]
   [clojure.core.async :refer [chan unique dropping-buffer 
                               go >! <! mult filter> tap]]
   [clojure.pprint :refer :all]
   [clojure.core.typed :as t :refer :all]))

(def-alias EventChan (Chan (Event)))

(defn do-event [w e c]
  (pprint e)
  (when-let [clean-e (check-event w e c)]
                (or (run-event w clean-e c) w)))


(ann  start-world [World ->  (Vec WorldChan 
                                         StateChan 
                                         EventChan)])
(defn start-world [world ]
      (let [c (chan 500)
            wchan (unique (chan (dropping-buffer 1)))
            schan (unique (chan (dropping-buffer 1)))]
        (go (loop [w world]
            (pprint w)
                 (>! wchan (:map world))
                 (>! schan (:state))
                (recur (or (do-event w (<! c) c) w))))
        [c wchan schan]))


