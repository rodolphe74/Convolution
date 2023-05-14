# Convolution
A threaded kernel convolution implementation in Clojure. Project created with Leiningen.

<img src="doc/Clojure_logo.png" height="150"> <img src="doc/leiningen.jpg" height="150">

'''
  (init "resources/c.png")
  (time (process-kernel sharp-k sharp-d))
  (finish-him "resources/c-sharpen.png")
'''

# Principle
- [Wiki](https://en.wikipedia.org/wiki/Kernel_(image_processing))

# Librairies
- [ImageZ](https://github.com/mikera/imagez).
- [Claypoole](https://github.com/clj-commons/claypoole).
