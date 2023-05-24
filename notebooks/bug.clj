(ns bug
  #:nextjournal.clerk{:visibility {:code :hide}}
  (:require
   [clojure.java.io :as io]
   [nextjournal.clerk :as clerk]
   [nextjournal.clerk-slideshow :as slideshow])
  (:import
   (javax.imageio ImageIO)))



(ImageIO/read (io/file "./resources/jcip.jpg"))
