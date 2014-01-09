(ns globe.engine)
(defn point [x y])

(defn random-point [maxx maxy]
   [(mod rand-int maxx) (mod rand-int maxy)])

(defn ceil [x limit]
  (if (<= x limit) x limit))

(defn random-square [[maxx maxy] maxsize]
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

(defn midpoint [xpoint ypoint]
  {:pre (reduce (partial > 0) (flatten [xpoint ypoint]))}
  (map  #(if (> % 0) (quot % 2) 0 ) (map + xpoint ypoint)))


(defn up [lower])

(defn generate-dungeon [{:keys [dims max-room max-branch max-hall] }]
  (let [start-room (random-square dims max-room) ])
  (for [x (range (rand-int max-branch))]
    (random-dir)
    )  
  )
