(ns globe.core
  (:require
   [clojure.core.async :refer [chan go >!! <!! mult filter> tap]]
   [globe.engine  :as engine :refer :all]
   [globe.world   :as world :refer :all]))



(def world-chan (start-world world/empty-world))

(engine/do-event world/empty-world [:player [:tic []] 0] (chan))

(>!! world-chan  [:player [:tic []] 1] )

(defn add-base-event [obj]
  (>!! world-chan  [:player [:add-base [obj]] 3] ))

(def add-things
 (do (add-base-event {:ables #{:visable}
                   :stats  {:name "wood floor"
                            :char '*
                            :prefix "wdf"
                            :sym :wood-floor
                            :hp 100}} )

     (add-base-event {:ables #{:visable :collideable}
                   :stats  {:name "stone wall"
                            :char 'x
                            :prefix "stw"
                            :sym :stone-wall
                            :hp 1000}})

     (add-base-event {:ables #{:visable :collideable}
                   :stats  {:name "stone statue"
                           :char 's
                           :prefix "sts"
                           :sym :stone-statue
                           :hp 50}})))
