(ns brave-true-tut.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def asym-hobbit-body-parts
  [{:name "head", :size 3}
   {:name "left-eye", :size 1}
   {:name "left-ear", :size 1}
   {:name "mouth", :size 1}
   {:name "nose", :size 1}
   {:name "neck", :size 2}
   {:name "left-shoulder", :size 3}
   {:name "left-upper-arm", :size 3}
   {:name "chest", :size 10}
   {:name "back", :size 10}
   {:name "left-forearm", :size 3}
   {:name "abdomen", :size 6}
   {:name "left-kidney", :size 1}
   {:name "left-hand", :size 2}
   {:name "left-knee", :size 2}
   {:name "left-thigh", :size 4}
   {:name "left-lower-leg", :size 3}
   {:name "left-achilles", :size 1}
   {:name "left-foot", :size 2}
   ]
)

(defn has-matching-part?
  [part]
  (re-find #"^left-" (:name part)))

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   ;; ........................................^^^^^^^... THAT IS A LITERAL REGEX, *NOT* AN ANONYMOUS FN!
   :size (:size part)})

;; I can feel in my bones there's a more elegant way to do this, but my clojure foo can't determine it yet
(defn symmetrize-body-parts
  "Expects a seq of maps which have a :name and :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts
            final-body-parts (conj final-body-parts part)]
        (if (has-matching-part? part)
          (recur remaining (conj final-body-parts (matching-part part)))
          (recur remaining final-body-parts))))))

(symmetrize-body-parts asym-hobbit-body-parts)


;; aha, yes. reduce is one way to simplify and shorten it!

(defn charlie
  "Charlie reqrite using reduce"
  [asym-body-parts]
  (let [f (fn [xs x]
            (let [ys (conj xs x)]
              (if (has-matching-part? x)
                (conj ys (matching-part x))
                ys)))]
    (reduce f [] asym-body-parts)))

;; Sweet! I got same thing author did, except he put f inline.

(defn hit
  [asym-body-parts]
  (let [sym-parts (charlie asym-body-parts)
        size-sum (reduce + 0 (map :size sym-parts))
        target (inc (rand size-sum))]
    (loop [[x & xs] sym-parts
           accumulated-size (:size x)]
      (if (> accumulated-size target)
        x
        (recur xs (+ accumulated-size (:size x)))))))
