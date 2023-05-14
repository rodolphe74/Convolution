# Convolution
A threaded kernel convolution implementation in Clojure. Project created with Leiningen.

<img src="doc/Clojure_logo.png" height="150"> <img src="doc/leiningen.jpg" height="150">


```clojure
  (init "resources/c.png")
  (time (process-kernel sharp-k sharp-d))
  (finish-him "resources/c-sharpen.png")
```

<img src="doc/crop_c.png" height="154" >       <img src="doc/crop_c_sharpen.png" height="154">


# Principle
- [Wiki](https://en.wikipedia.org/wiki/Kernel_(image_processing))

# Librairies
- [ImageZ](https://github.com/mikera/imagez).
- [Claypoole](https://github.com/clj-commons/claypoole).
