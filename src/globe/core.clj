(ns globe.core
  (:require
   [clojure.core.async :refer [chan go >!! <!! mult filter> tap]]
   [globe.engine  :as engine :refer :all]
   [globe.world   :as world :refer :all]))



(def world-chan (start-world world/empty-world))

(engine/do-event world/empty-world [:player [:tic []] 0] (chan))


(>!! world-chan  [:player [:tic []] 0] )
