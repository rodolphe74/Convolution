(ns convolution.core
  (:gen-class)
  (:require [mikera.image.core :as m]
            [mikera.image.colours :as c]
            [clojure.core.matrix :as x]))

(def src-image (ref nil))
(def target-image (ref nil))

(def sharp-k [[0.0 -1.0 0.0]
              [-1.0 5.0 -1.0]
              [0.0 -1.0 0.0]])
(def sharp-d 1.0)

(def gaussian-blur-k [[1.0 2.0 1.0]
                      [2.0 4.0 2.0]
                      [1.0 2.0 1.0]])
(def gaussian-blur-d 16.0)

(def blur-k [[1.0 1.0 1.0]
             [1.0 1.0 1.0]
             [1.0 1.0 1.0]])
(def blur-d 9.0)

(def mat-33 [[[-1 -1] [0 -1] [1 -1]]
             [[-1 0] [0 0] [1 0]]
             [[-1 1] [0 1] [1 1]]])

(defn init []
  (dosync (ref-set src-image
                   (m/load-image "resources/mob.png")))
  (dosync (ref-set target-image
                   (m/new-image (m/width @src-image) (m/height @src-image)))))

(defn get-int [rgba-value]
  (let [r (bit-and (bit-shift-left (nth rgba-value 0) 16) 0x00FF0000)
        g (bit-and (bit-shift-left (nth rgba-value 1) 8) 0x0000FF00)
        b (bit-and (nth rgba-value 2) 0x000000FF)]
    (bit-or 0xFF000000 r g b)))

(defn process-kernel [k d]
  (let [l (x/div k d) mx (atom 0)]
    (println  k  '/ d '= l)

    (doseq [y (range 0 (m/height @src-image))
            x (range 0 (m/width @src-image))]

      (let [a [0 0 0 255]
            b (atom a)]
        ;(println 'xy x y)
        (doseq [my (range (nth  (x/shape mat-33) 1))
                mx (range (nth (x/shape mat-33) 0))]

          (try (let [s (nth (nth mat-33 my) mx)
                     q (c/components-argb (m/get-pixel @src-image (+ x (nth s 0)) (+ y (nth s 1))))
                     r (nth (nth l my) mx)
                     t (into [] [r r r 0])
                     c (into [] (map * q t))]

                 (swap! b #(map + c %1)))
               (catch IndexOutOfBoundsException e
                 ; out of image - ignoring
                 )))



        ; find max value 


        (reset! mx (max @mx (apply max (take 3 @b))))

        (reset! b (into [] (map #(if (> %1 255) 255 %1) (into [] @b))))
        (reset! b (into [] (map #(if (< %1 0) 0 %1) (into [] @b))))

        (let [u (get-int (map int @b))]
          (m/set-pixel @target-image x y u))))

    (println 'max 'value @mx)))

(defn finish-him []
  (println 'saving)
  (m/save @target-image "resources/c-sharp.png" :quality 0.7 :progressive true))

(defn -main
  "Convolution image filter"
  [& args]
  (init)
  ;(m/show @src-image)
  ;(process-kernel sharp-k sharp-d)
  ;(process-kernel blur-k blur-d)
  (process-kernel gaussian-blur-k gaussian-blur-d)

  (finish-him))
