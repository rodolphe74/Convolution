(ns convolution.core
  (:gen-class)
  (:require [mikera.image.core :as m]
            [mikera.image.colours :as c]
            [clojure.core.matrix :as x]
            [com.climate.claypoole :as cp]))

(def src-image (ref nil))
(def target-image (ref nil))

(def pool (cp/threadpool (cp/ncpus)))
(def wait (atom 1))

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

(defn init [image-filename]
  (dosync (ref-set src-image
                   (m/load-image image-filename)))
  (dosync (ref-set target-image
                   (m/new-image (m/width @src-image) (m/height @src-image)))))

(defn get-int [rgba-value]
  (let [r (bit-and (bit-shift-left (nth rgba-value 0) 16) 0x00FF0000)
        g (bit-and (bit-shift-left (nth rgba-value 1) 8) 0x0000FF00)
        b (bit-and (nth rgba-value 2) 0x000000FF)]
    (bit-or 0xFF000000 r g b)))

(defn process-kernel-at [k d l x y stopx stopy]
  (let [a [0 0 0 255]
        b (atom a)]
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

    (reset! b (into [] (map #(if (> %1 255) 255 %1) (into [] @b))))
    (reset! b (into [] (map #(if (< %1 0) 0 %1) (into [] @b))))

    (let [u (get-int (map int @b))]
      (m/set-pixel @target-image x y u))

    (if (and (== x (- stopx 1)) (== y (- stopy 1)))
      (reset! wait 0))))

(defn process-kernel [k d]
  (let [l (x/div k d) mx (atom 0)]
    (println  k  '/ d '= l)

    (doseq [y (range 0 (m/height @src-image))
            x (range 0 (m/width @src-image))]

      (cp/future pool (process-kernel-at k d l x y (m/width @src-image) (m/height @src-image))))

    (while (= @wait 1)
      (Thread/sleep 100))))

(defn finish-him [image-filename]
  (println 'saving)
  (m/save @target-image image-filename :quality 0.8 :progressive true))

(defn -main
  "Convolution image filter"
  [& args]

  (println 'using (cp/ncpus) 'threads)

  (init "resources/c.png")
  ; (m/show @src-image)
  (time (process-kernel gaussian-blur-k gaussian-blur-d))
  (finish-him "resources/c-gaussian-blur.png")

  (init "resources/c.png")
  (time (process-kernel sharp-k sharp-d))
  (finish-him "resources/c-sharpen.png")

  (init "resources/c.png")
  (time (process-kernel blur-k blur-d))
  (finish-him "resources/c-blur.png"))
