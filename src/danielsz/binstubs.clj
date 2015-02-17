(ns danielsz.binstubs
  {:boot/export-tasks true}
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [boot.core :as core]
            [me.raynes.fs :as fs]))

(def current-dir (System/getProperty "user.dir"))
(def path (System/getenv "PATH"))
(def user-directory (System/getProperty "user.home"))
(def boot-bin-directory (io/file (str user-directory "/.boot/bin")))

(defn write-executable [lines path]
  (io/make-parents path)
    (with-open [wrtr (io/writer path)]
      (doseq [line lines]
        (.write wrtr (str line "\n"))))
    (fs/chmod "+x" path))

(defmacro check-precondition [condition err-msg]
  `(when-not ~condition
     (binding [*out* *err*]
       (println ~err-msg)
       (System/exit -1))))
 
(core/deftask binstub
  "If conditions are met, will stub an executable in boot/bin directory."
  [r release RELEASE str "The final name of your application stub"]
  (fn [next-task]
    (fn [fileset]
      (check-precondition ((every-pred #(.exists %) #(.isDirectory %)) boot-bin-directory) "./boot/bin does not exist")
      (check-precondition (boolean (re-find #"/.boot/bin" path)) "./boot/bin is not in your path")
      (check-precondition (.exists (io/file "build.boot")) "Can't find build.boot in current directory")
      (check-precondition (not (s/blank? release)) "Please provide a name for your stub. Type \"boot binstub -h\" for help ")
      (let [lines ["#!/bin/sh -e"
                   (str  "cd " current-dir " && " "./build.boot")]
            path (str boot-bin-directory "/" release) ]
        (write-executable lines path))
      (next-task fileset))))
