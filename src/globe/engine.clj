(ns globe.engine
   (:require
   [globe.events :refer :all]
   [clojure.core.incubator :refer :all]
   [clojure.core.async :refer [chan unique dropping-buffer 
                               go >! <! mult filter> tap]]
   [clojure.pprint :refer :all]
    [midje.sweet :refer :all]
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

(defn point [x y])

(defn random-point [maxx maxy]
   [(mod rand-int maxx) (mod rand-int maxy)])

(defn ceil [x limit]
  (if (<= x limit) x limit))

(defn random-square [maxx maxy maxsize]
  (let [ [x y] (random-point)
        px (rand-int maxsize)
        py (rand-int maxsize)]
     [[x y] [(ceil px maxx) (ceil py maxy)]]))

(defn top [[[lx ly] [ux uy]]]
  [[lx uy] [ux uy]])

(defn bottom [[[lx ly] [ux uy]]]
  [[lx ly] [ux ly]])

(defn right [[[lx ly] [ux uy]]]
  [[ux ly] [ux uy]])

(defn left [[[lx ly] [ux uy]]]
  [[ux ly] [ux uy]])

(def dirs [top bottom right left])

(defn doors [size]
    (rand-int (quot size 3 )))

(defn random-dir [] (get dirs (rand-int 4)))
(defn rand-midpoint [lpoint rpoint]
     
 )

(defn midpoint [xpoint ypoint]
  {:pre (reduce (partial > 0) (flatten [xpoint ypoint]))}
  (map  #(if (> % 0) (quot % 2) 0 ) (map + xpoint ypoint)))

(defn up [lower])

(defn generate-dungeon [size [d w f e]])
