(ns bug
  #:nextjournal.clerk{:visibility {:code :hide}}
  (:require
   [clojure.java.io :as io]
   [nextjournal.clerk :as clerk])
  (:import ;;[java.net URL]
           ;;[java.nio.file Paths Files]
           ;;[java.awt.image BufferedImage]
   (javax.imageio ImageIO)))











(ImageIO/read (io/file "./resources/jcip.jpg")
              #_(java.net.URL. "https://nextjournal.com/data/QmeyvaR3Q5XSwe14ZS6D5WBQGg1zaBaeG3SeyyuUURE2pq?filename=thermos.gif&content-type=image/gif"))
