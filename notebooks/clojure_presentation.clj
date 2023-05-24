^{:nextjournal.clerk/visibility {:code :hide :result :hide}}
(ns clojure-presentation
  (:require [clojure.java.io :as io]
            [nextjournal.clerk :as clerk]
            [nextjournal.clerk.viewer :as v]
            [nextjournal.clerk-slideshow :as slideshow])
  (:import [javax.imageio ImageIO]))

^{::clerk/visibility {:code :hide :result :hide}}
(clerk/add-viewers! [slideshow/viewer])


^{::clerk/visibility {:code :hide :result :hide}}
(def editor-sync-viewer
  {:evaluator :sci
   :render-fn
   '(fn [value]
      (let [safe-load-string (fn [s]
                               (try
                                 (let [ret (load-string s)]
                                   (if (seq? ret)
                                     (try
                                       (first ret)
                                       ret
                                       (catch :default e
                                         e))
                                     ret))
                                 (catch :default e
                                   e)))

            !input (reagent.core/atom value)
            !val (reagent.core/atom (safe-load-string @!input))
            on-change (fn [code]
                        (reset! !input code)
                        (reset! !val (safe-load-string @!input)))]
        (fn [value]
          [:div
           [:div.flex.space-x-2
            [:div.viewer-code.bg-slate-50.full
             [nextjournal.clerk.render.code/editor
              !input
              {:on-change on-change}]]

            [:span.prose
             " => "

             [nextjournal.clerk.render/inspect
              @!val]]]])))})

^{::clerk/visibility {:code :hide :result :hide}}
(def mermaid-viewer
  {:transform-fn clerk/mark-presented
   :render-fn '(fn [value]
                 (when value
                   [nextjournal.clerk.render/with-d3-require {:package ["mermaid@8.14/dist/mermaid.js"]}
                    (fn [mermaid]
                      [:div {:ref (fn [el] (when el
                                             (.render mermaid (str (gensym)) value #(set! (.-innerHTML el) %))))}])]))})


;; ---
;; ![](https://ask.clojure.org/icons/clojure200.png)
;; # Functional Programming Clojure at TPXImpact
;;
;; Rick Moynihan
;;
;; Head of Engineering (Data & Insights)

;; ---
;; # 👋 About me
;;
;; - Over 20 years Software Engineering
;; - Worked for
;;   - 4 startups
;;   - One agency
;; - Spent the last decade at Swirrl
;;    - Working on data infrastructure & services for government
;;
;; - ... and now TPXImpact


;; ---
;; # I want to convince you of three things

;; ---
;; # 1. Clojure is a boring language

;; ---
;; # 1. Clojure is a boring language
;;
;; _* boring enough that you could possibly use it at work_


;; ---
;; # 2. Clojure is full of big contrarian ideas, which are interesting and exciting...

;; ---
;; # 2. ... and learning it may radically change your approach to software development and data _*_
;;
;; _* for the better_

;; ---
;; # 3. And finally that programming in Clojure is a lot of fun


;; ---
;; # What is Clojure?
;;
;; - A modern functional programming language in the Lisp family of languages
;; - Created in 2007
;; - Open source
;; - Compiles to Java and Runs on the JVM
;; - Dynamically typed
;; - Created and led by Rich Hickey
;; - Has a strong development philosophy behind it

;; ---
;; # What else is Clojure?
;;
;; - ... a small but vibrant open-source community
;; - a dialect of similar "hosted languages"
;;    - [Clojure](https://clojure.org/)
;;    - [Clojurescript](https://clojurescript.org/)
;;    - [Small Clojure Interpreter](https://github.com/babashka/sci) & [Babashka](https://babashka.org/)
;;    - [ClojureCLR](https://github.com/clojure/clojure-clr)
;;    - [Clojure Dart](https://github.com/Tensegritics/ClojureDart)

;; ---
;; # What else is Clojure?
;;
;; - Many big ideas carefully composed together
;; - A case study in simplicity, elegance and design

;; ---
;; # Chapter 1: A boring language

;; ---
;; # Chapter 1: A boring language
;;
;; - Most of Clojure Core Team work for Nubank
;; - Nubank have built their business on Clojure (thousands of systems)
;; - It's reliable and predictable
;; - Host language (java) interop is great so there are usable libraries available for almost everythingb

;; ---
;; # Chapter 1: A boring language
;;
;; - It changes slowly and purposefully (roughly one stable release p/year)
;;   - Improvements are well thought out and conservative
;; - Follows a strong philosophy of not making breaking changes
;;   - See Rich Hickey's talk [Spec-ulation](https://www.youtube.com/watch?v=oyLBGkS5ICk)
;;   - Two kinds of change
;;     - Growth (accretion, relaxation, fixation)
;;     - Breakage (requiring more or providing less)
;;   - Embodied in tools.deps (package manager) version resolution
;;   - language features like namespaced keywords / clojure.spec etc
;; - The libraries and community tend to follow this advice
;;   - It's not unusual to update every dependency in a legacy apps and have them still work

;; ---
;;
;; # Chapter 2: Contrarian, interesting and exciting


;; ---
;; # Big ideas
;;
;; 1. Lisp
;; 2. The value of values
;; 3. Programming as a dialogue
;; 4. Simplicity (a rigorous definition)
;; 5. Inside out programming
;; 6. Homoiconic Representation
;; 7. Hosted languages
;; 8. Move slow and purposefully, don't break things



;; ---
;;
;; # Chapter 2.1: The legends of Lisp

;; ---
;; # A biased history of Programming Languages*
;;
^{::clerk/visibility {:code :hide}}
(clerk/with-viewer mermaid-viewer {:nextjournal.clerk/width :full}
"gantt
  dateFormat YYYY
  axisFormat %Y

  section Functional
    Lambda Calculus : milestone, m1, 1930, 2min
    Theory of types : milestone, m3, 1940, 2min
    Lisp (John Mccarthy): milestone, m5, 1958, 2min
    ML: milestone, m6, 1973, 2min
    Lisp Machine: milestone, m6, 1973, 2min
    Scheme: milestone, m6, 1975, 2min
    Hindley-Milner Type System: milestone, m11, 1978, 2min
    Haskell: milestone, m7, 1990, 2min
    Clojure: milestone, m8, 2007, 2min

  section Imperative
    Turing Machine : milestone, m2, 1936, 2min
    Fortran : milestone, m4, 1956, 2min
    C : milestone, m9, 1972, 2min
    Smalltalk : milestone, m112, 1972, 2min
    Java / JVM : milestone, m10, 1995, 2min
    Rust : milestone, m13, 2010, 2min
")
;; _* The majority of the world focussed on the mechanistic/imperative approach, but there was huge innovation and clarity of thinking on the functional side_

;; ---
;; # Lisp (1956) John Mccarthy
;;
;; ![](https://blog.allganize.ai/content/images/size/w600/2020/08/john-mccarthy-stanford.jpg)

;; ---
;; # Lisp / Scheme
;;
;; - LISt Processing
;; - Based on the lambda calculus
;; - Is really a whole family of similar languages
;; - Arguably the purest most elegant expression of a nice programming language*
;;
;; _*_ _though there are others_

;; ---
;;
;; ![](https://michaelnielsen.org/ddi/wp-content/uploads/2012/04/Lisp_Maxwells_Equations.png)
;;
;; > Maxwells Equations of Software
;;
;; -- Alan Kay

;; ---
;; # What Learning a Lisp teaches you
;;
;; - Embodies the essential minimal essence of what it means to compute
;; - Makes it trivially easy to extend the language and compiler
;; - Liberates programming language development out of the domain of computer scientists in ways practitioners can understand.  See the book [Lisp in Small Pieces](https://www.amazon.co.uk/Lisp-Small-Pieces-Christian-Queinnec/dp/0521545668)
;;    - A practical example of this is [core.async](https://github.com/clojure/core.async/) which implements go's CSP as a library in Clojure!
;;    - Or see our colleague, Andrew Mcveigh's talk [on adding a type checker](https://skillsmatter.com/skillscasts/10835-a-dynamic-statically-typed-contradiction) to Clojure

;; ---
;; # Eval / Apply
;; ![](https://user-images.githubusercontent.com/223396/64483083-67645500-d1c2-11e9-8dec-b8f34828048c.jpg)
;; See also Paul Graham's [Roots of Lisp](http://www.paulgraham.com/rootsoflisp.html)

;; ---
;; # Lisp
;;
;; - Became 'popular' in the 1980's
;; - Pionering work on Software & development environments
;;   - Still unrivaled today

;; ---
;; - Lisp Machine's and the retro sci-fi future we never had...
;;   - [Symbolics Lisp Machines Graphics Demo (1990)](https://news.ycombinator.com/item?id=36016649)

;; ---
;; # Lisp Machines
;; The Connection Machine CM-1
;;
;; ![CM-1](https://www.teco.kit.edu/wp-content/uploads/2014/09/10606268_829432493743897_7185944170258321804_n.jpg)

;; ---
;; # Some big ideas in Lisp (and Clojure)
;;
;; - Minimal elegant core
;; - Building programs from inside your program as a dialogue with your computer (REPL)
;; - Homoiconic
;;    - single representation for code and data
;; - Macros
;;

;; ---
;; # More legends of Lisp
;;
;; - [Lisp at Nasa's JPL](https://corecursive.com/lisp-in-space-with-ron-garret/)
;; - [Essays of Richard P. Gabriel](https://dreamsongs.com/Essays.html)
;;   - [Worse is better](https://dreamsongs.com/WorseIsBetter.html)
;;     - [Worse is better is worse], [Is worse really better?](https://dreamsongs.com/Files/IsWorseReallyBetter.pdf), [Is worse (still) better?](https://dreamsongs.com/Files/WorseIsBetterPositionPaper.pdf), [Worse (still) is better!](https://dreamsongs.com/Files/ProWorseIsBetterPosition.pdf)
;;   - [The art of lisp & Writing](https://dreamsongs.com/ArtOfLisp.html)
;;   - [The why of Y](https://dreamsongs.com/Files/WhyOfY.pdf)
;; - Paul Graham [Beating the averages](http://paulgraham.com/avg.html)
;;   - [... other Lisp essays](http://paulgraham.com/articles.html)
;; - [GNU Project](https://www.gnu.org/gnu/initial-announcement.html)
;; - [The development of GNU Emacs](https://www.gnu.org/gnu/rms-lisp.html)

;; ---
;;
;; ![God wrote in Lisp](https://www.explainxkcd.com/wiki/images/9/9a/lisp.jpg)
;;

;; ---
;; # So what is Clojure?
;;
;; - Clojure is a Lisp
;; - ...but is not a direct descendant of any prior Lisp
;; - Unlike historic Lisps it
;;   - Focuses on Functional Programming with immutable data
;;   - is intentionally hosted, in that it compiles to and runs on the runtime of another language, such as the JVM


;; ---
;; # 2.2: The value of values
;;
;; What is a value?
;;
;; Something that doesn't change!  Examples:

1




;; ---
;; # 2.2: The value of values
;;
;; - Immutable Persistent Datastructures

^{::clerk/visibility {:code :hide}}
(clerk/with-viewer mermaid-viewer {:nextjournal.clerk/width :full}
  "flowchart LR
    Alice{Alice} --> A
    A --> B
    B --> C
    C --> D
")

;; ---
;; # 2.2: The value of values
;;
;; - Immutable Persistent Datastructures

^{::clerk/visibility {:code :hide}}
(clerk/with-viewer mermaid-viewer {:nextjournal.clerk/width :full}
  "flowchart LR
    Alice{Alice} --> A
    Bob{Bob} --> A
    A --> B
    B --> C
    C --> D
")

;; ---
;; # 2.2: The value of values
;;
;; - Immutable Persistent Datastructures

^{::clerk/visibility {:code :hide}}
(clerk/with-viewer mermaid-viewer {:nextjournal.clerk/width :full}
  "flowchart LR
    Alice{Alice} --> A
    A --> B
    B --> C
    C --> D
")




;; ---
;;
;; # Clojure features


;; ---
;; # TODO

;; ---
;; # clojure data

1
1/2
1.25

"Strings"

:keywords
symbol

'quoted-sym

;; # Collection types

[:vectors :are :like "arrays"]

;; Vectors grow at the end

'(:linked :lists)

;; Linked lists naturally grow at the start

;; Hashmaps

{:key1 "value 1"
 :key2 "value 2"}

;; Sets

#{1 2 3}

;; ---
;; # Laziness
;;
;;

^{::clerk/visibility {:code :hide}}
(clerk/with-viewer editor-sync-viewer
  "(map inc [1 2 3 4])")

^{::clerk/visibility {:code :hide}}
(clerk/with-viewer editor-sync-viewer
  "(map str [1 2 3 4])")


(range)

(repeat 100 :thing)

(iterate inc 0)

(map vector
     (range)
     (concat
      (repeat 10 :a)
      (repeat 10 :b)
      (repeat 10 :c)))

(defn spreadsheet-columns
  "Given an alphabet string generate a lazy sequences of column names
  e.g.

  `(column-names-seq \"abcdefghijklmnopqrstuvwxyz\") ;; => (\"a\" \"b\" \"c\" ... \"aa\" \"ab\")`"
  [alphabet]
  (->> (map str alphabet)
       (iterate (fn [chars]
                  (for [x chars
                        y alphabet]
                    (str x y))))
       (mapcat identity)))

(take 30 (drop 20 (spreadsheet-columns "abcdefghijklmnopqrstuvwxyz")))




;; ---
;; # Books / Resources
;;
;; - ![Getting Clojure](https://pragprog.com/titles/roclojure/getting-clojure/roclojure.jpg)
;; - ![The Joy of Clojure](http://blog.fogus.me/images/joy-of-clojure-cover.jpg)

;; ---
;; # Free / Resources
;; - ![Clojure for the Brave and True](https://www.braveclojure.com/assets/images/home/book-cover.jpg)
;; - [clojure.org](https://clojure.org/)
;;   - ![Clojure Reference](https://clojure.org/reference)
;;   - ![Clojure Guides](https://clojure.org/guides)

;; ---
;; # Boring can be Interesting
;;
;; TODO
;;


;;

;; ---
;; # Cool ideas
;;
;; - Functional Programming
;; - Simplicity (but a rigorous definition)
;; - Open for extension
;; -
;; - Laziness
;; - Immutability Changes Everything (& Persistent Data structures)
;; -
;; - Epochal time model
;; - Equality done right
;; - Meta circular evaluators


;; ---
;; # TODO
;;

;; ---
;; # TODO


;; ---
;; # What next?
;;
;; - We're keen to promote functional programming and Clojure within TPX, if you're interested `#cop_tech_fp`.
