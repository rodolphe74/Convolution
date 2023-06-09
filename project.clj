(defproject convolution "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [net.mikera/imagez "0.12.0"]
                 [net.mikera/core.matrix "0.63.0"]
                 [org.clj-commons/claypoole "1.2.2"]]

  ; jit compilation
  :main ^:skip-aot convolution.core

  ; aot compilation
  ;:main convolution.core
  ;:aot [convolution.core]
  
  :plugins [[lein-cljfmt "0.6.4"]]
 
  :target-path "target/%s"

  ; remove dynamic runtime
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  )
