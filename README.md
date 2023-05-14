# Convolution
A threaded kernel convolution implementation in Clojure. Project created with Leiningen.

<img src="doc/Clojure_logo.png" height="150"> <img src="doc/leiningen.jpg" height="150">


```clojure
  (init "resources/c.png")
  (time (process-kernel sharp-k sharp-d))
  (finish-him "resources/c-sharpen.png")
```

<img src="doc/crop_c.png"  width="50%" height="50%" > <img src="doc/crop_c_sharpen.png"  width="50%" height="50%">


# Principle
- [Wiki](https://en.wikipedia.org/wiki/Kernel_(image_processing))

# Librairies
- [ImageZ](https://github.com/mikera/imagez).
- [Claypoole](https://github.com/clj-commons/claypoole).
