(ns binstubs
  (:require [clojure.java.io :as io]
            [me.raynes.conch :refer [programs with-programs let-programs] :as sh]))

(def current-dir (System/getProperty "user.dir"))
(def path (System/getenv "PATH"))
(def user-directory (System/getProperty "user.home"))
(def boot-directory (io/file (str user-directory "/.boot/bin")))

(defmacro check-precondition [condition err-msg]
  `(when-not ~condition
     (binding [*out* *err*]
       (println ~err-msg))))

(check-precondition ((every-pred #(.exists %) #(.isDirectory %)) boot-directory) "./boot/bin does not exist")
(check-precondition (boolean (re-find #"/.boot/bin" path)) "./boot/bin is not in your path")
(check-precondition (.exists (io/file "build.boot")) "Can't find build.boot in current directory")
