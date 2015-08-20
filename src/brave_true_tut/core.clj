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

(defn hit-reduce [asym-body-parts]
  (let [sym-parts (charlie asym-body-parts)
        size-sum (reduce + 0 (map :size sym-parts))
        target (inc (rand size-sum))]
    (reduce
     (fn [accsize part]
       (if (:size accsize)
         part
         (let [newsize (+ accsize (:size part))]
           (if (> newsize target)
             part
             newsize)))
       ) 0 sym-parts)))

(defn big-list
  []
  (charlie asym-hobbit-body-parts))


(matching-part {:name "left-ass-cheek" :size 42})

; Rest of this file is Joy of Clojure experimentation in the repl
;; (defn xors [max-x max-y]  (for [x (range max-x) y (range max-y)] [x y (bit-xor x y)]))

;; (bit-xor 1 2)

;; (xors 2 2)

;; (def frame (java.awt.Frame.))

;; frame

;; (for [method (seq (.getMethods java.awt.Frame))
;;       :let [method-name (.getName method)]
;;       :when (re-find #"Vis" method-name)]
;;   method-name)

;; (.isVisible frame)

;; (.setVisible frame true)
;; (.setSize frame (java.awt.Dimension. 256 256))
;; (.setLocation frame 300 300)

;; (def gfx (.getGraphics frame))

;; (.fillRect gfx 100 100 50 75)

;; (.setColor gfx (java.awt.Color. 255 128 0))
;; (.fillRect gfx 100 150 75 50)

(defn neighbors
  ([size yx] (neighbors [[-1 0] [1 0] [0 -1] [0 1]] size yx))
  ([deltas size yx]
     (filter (fn [new-yx]
               (every? #(< -1 % size) new-yx))
             (map #(map + yx %) deltas))))
;; (peek)

;; (pop)

;; (last)
(def schedule
  (conj clojure.lang.PersistentQueue/EMPTY
        :wake-up :shower :brush-teeth))

(defn shite
  []
  (println "shite!"))

(defn shitola
  []
  (println "shitola!"))


(defn random-ints
  [limit]
  (lazy-seq
   (println "realizing random number")
   (cons (rand-int limit)
         (random-ints limit))))

;; (def rands (take 10 (random-ints 50)))

;; (nth rands 3)

(def foo (next (random-ints 50)))

(println "done with foo")

(def bar (rest (random-ints 50)))

(println "done with bar")

;; Now, from Clojure Programming, the Show Info example
(ns com.clojurebook.fn-browser
  (:import (javax.swing JList JFrame JScrollPane JButton)
           java.util.Vector))

(defonce fn-names (->> (ns-publics 'clojure.core)
                       (map key)
                       sort
                       Vector.
                       JList.))

(defn show-info [] )

(defonce window (doto (JFrame. "\"Interactive Development!\"")
                  (.setSize (java.awt.Dimension. 400 300))
                  (.setLocation 300 300)
                  (.add (JScrollPane. fn-names))
                  (.add java.awt.BorderLayout/SOUTH
                        (doto (JButton. "Show Info")
                          (.addActionListener (reify java.awt.event.ActionListener
                                                (actionPerformed [_ e] (show-info))))))
                  (.setVisible true)))
