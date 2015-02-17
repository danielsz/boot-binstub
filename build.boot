(set-env!
  :source-paths #{"src"}
  :dependencies '[[me.raynes/fs "1.4.6"]
                  [boot/core "2.0.0-rc9" :scope "provided"]])

(task-options!
 aot {:namespace '#{danielsz.binstubs}}
 pom {:project 'danielsz/boot-binstubs
      :version "0.0.1-SNAPSHOT"})

(deftask build
  "Build jar and install to local repo."
  []
  (comp (aot) (pom) (jar) (install)))
