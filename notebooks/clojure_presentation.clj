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


^{::clerk/visibility {:code :hide :result :hide}}
(def plt-history-timeline
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
    Fortran : milestone, m4, 1957, 2min
    C : milestone, m9, 1972, 2min
    Smalltalk : milestone, m112, 1972, 2min
    Java / JVM : milestone, m10, 1995, 2min
    Rust : milestone, m13, 2010, 2min
")

;; ---
;; ![Clojure Logo](https://ask.clojure.org/icons/clojure200.png)
;; # Opening Two Doors to Great Ideas
;; (Functional Programming & Clojure)
;;
;; Rick Moynihan
;;
;; Head of Engineering (Data & Insights)

;; ---
;; # `#cop_tech_fp`
;;
;; - A new community of practice, populated by myself and my colleagues
;; - To talk about Functional Programming (and adjacent topics)
;; - And spread the love of lambda
;; - Please join, and post any questions you have there
;; - I can stop and answer questions along the way

;; ---
;; # 👋 Who am I anyway?
;;
;; - Rick Moynihan (Head of Engineering in D&I)
;; - Over 20 years Software Engineering
;; - Worked for
;;   - 4 startups
;;   - One agency
;; - Spent the last decade at Swirrl
;;    - Working on data infrastructure & services for government
;;
;; - ... and now TPXImpact
;; - Have been using Clojure for real work since 2008

;; ---
;; # My Agenda
;;
^{::clerk/visibility {:code :hide}}
(clerk/with-viewer mermaid-viewer {:nextjournal.clerk/width :full}
  plt-history-timeline)
;; PL history has two streams (actually more but this is rhetoric, so I can lie and call it simplicity!)
;;
;; I'm assuming most people here, like most of the industry are on the imperative side.
;;
;; I'd like to do one thing, and excite, and encourage you to explore ideas on the functional side.

;; ---
;; # My impossible task
;;
;; - Functional Programming and Clojure are both golden gates into a rich seam of ideas.
;; - The ideas can challenge your deepest held assumptions about the craft of software,
;; - ...including assumptions on the nature of computation, design, systems, simplicity, and complexity itself.

;; ---
;; # (1962) Thomas Kuhn's theory of scientific revolutions
;;
;; - Introduced the ideas of Paradigms & Paradigm shifts

;; ---
;; # Incommensurability of paradigms
;;
;; - A barrier to crossing from the imperative paradigm to functional is that from the perspective of just one paradigm you can't objectively compare the other.
;; - You need to learn the other paradigm to discover what the fuss is about
;; - Learning paradigm's is hard because they fundamentally change your terms of reference
;;
^{::clerk/visibility {:code :hide}}
(clerk/with-viewer mermaid-viewer {:nextjournal.clerk/width :full}
  plt-history-timeline)

;; ---
;; # My impossible task
;;
;; - The other reason this is hard, is there's so much cool stuff to talk about!
;; - Potentially over 80 years worth of cool stuff!
;; - So I don't have time to go into anything too much
;; - I'm just going to throw lots of things I think are cool at you, and hope something resonates!

;; ---
;; # But maybe, it's easier than I make out...
;;
;; - ... because the stuff I'll show you is probably _simpler_ than you're used to 👍

;; ---
;; # but beware Simple != Easy
;;
;; - Rich Hickey (Clojure's creator) offers a [rigorous definition of simplicity](https://github.com/matthiasn/talk-transcripts/blob/master/Hickey_Rich/SimpleMadeEasy.md).
;; - We confuse Simple with Easy all the time
;; - To make things simple means:
;;   - To "decomplect", to tear apart, to isolate
;;   - individual concepts
;; - To make things easy means:
;;   - To have them to hand
;;   - To make them familiar
;;   - Frequently also to combine things together
;;
;; - Confusing these things causes huge problems
;;

;; ---
;; # Simple is often hard but not complex
;;
;; - You might simplify something by turning into 5 separate isolated things
;; - You will have more things to deal with
;; - But they will be more reusable, combinable, useful and understandable
;; - No silver bullets, but being mindful of these trade-offs prevents many problems

;; ---
;; # Simple Made Easy
;;
;; - If you only take away one idea, it should probably be this one
;; - Watch this talk or [read the transcript](https://github.com/matthiasn/talk-transcripts/blob/master/Hickey_Rich/SimpleMadeEasy.md)
;;
;;
^{::clerk/visibility {:code :hide}}
(clerk/html "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/LKtk3HCgTa8\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>")

;; ---
;; # Simple != Easy
;;
;; - It's no surprise that Rich Hickey (a Functional Programmer and Lisp developer) observed this
;; - As Lisp and the roots of Functional Programming implicitly treat simplicity in this way

;; ---
;; # Learning this stuff is a simple way to...
;;
;; 1. Sound a lot smarter than you are
;; 2. Eventually (with some effort) become smarter than you were
;;    - One day you wake up and realise you did things you thought would always be beyond you
;; 3. Gives you a new level of confidence

;; ---
;;
;; # The legends of Lisp

;; ---
;;
;; ![Lisp Cycles](https://imgs.xkcd.com/comics/lisp_cycles.png)


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
;; - Demystifies programming language theory.  See the book [Lisp in Small Pieces](https://www.amazon.co.uk/Lisp-Small-Pieces-Christian-Queinnec/dp/0521545668)

;; ---
;;
^{::clerk/visibility {:code :hide}}
(ImageIO/read (io/file "./resources/metacircular-evaluator.png"))

;; [Structure & Interpretation of Computer Programs](https://xuanji.appspot.com/isicp/)

;; See also Paul Graham's [Roots of Lisp](http://www.paulgraham.com/rootsoflisp.html)

;; ---
;;
;; ![God wrote in Lisp](https://www.explainxkcd.com/wiki/images/9/9a/lisp.jpg)
;;

;; ---
;; # Lisp: You can extend the language!
;; See our colleague, Andrew Mcveigh's talk [on adding an experimental type checker](https://skillsmatter.com/skillscasts/10835-a-dynamic-statically-typed-contradiction) to Clojure

^{::clerk/visibility {:code :hide}}
(ImageIO/read (io/file "./resources/andrew-types.png"))

;; ---
;; # Lisp: You can extend the language!
;; [core.async](https://github.com/clojure/core.async/) implements go's CSP as a library in Clojure!  Whilst Google needed to implement a whole new language to do this.
;;
;; See Timothy Baldridge's deep dive on [Core Async Go Macro Internals - Part I](https://www.youtube.com/watch?v=R3PZMIwXN_g) and [Part 2](https://www.youtube.com/watch?v=SI7qtuuahhU)
^{::clerk/visibility {:code :hide}}
(clerk/code
 "(let [c1 (chan)
      c2 (chan)]
  (go (while true
        (let [[v ch] (alts! [c1 c2])]
          (println \"Read\" v \"from\" ch))))
  (go (>! c1 \"hi\"))
  (go (>! c2 \"there\")))")


;; ---
;; # Lisp Machine's and the sci-fi future we never had...
^{::clerk/visibility {:code :hide}}
(clerk/row
 (ImageIO/read (io/file "./resources/s-graphics.png"))
 (ImageIO/read (io/file "./resources/lisp-debugger.png")))

;; [Symbolics Lisp Machines Graphics Demo (1990)](https://news.ycombinator.com/item?id=36016649)

;; ---
;; # More legends of Lisp
;;
;; - [Lisp at Nasa's JPL](https://corecursive.com/lisp-in-space-with-ron-garret/)
;; - [Essays of Richard P. Gabriel](https://dreamsongs.com/Essays.html)
;;   - [Worse is better](https://dreamsongs.com/WorseIsBetter.html)
;;     - [Worse is better is worse](https://www.dreamsongs.com/Files/worse-is-worse.pdf), [Is worse really better?](https://dreamsongs.com/Files/IsWorseReallyBetter.pdf), [Is worse (still) better?](https://dreamsongs.com/Files/WorseIsBetterPositionPaper.pdf), [Worse (still) is better!](https://dreamsongs.com/Files/ProWorseIsBetterPosition.pdf)
;;   - [The art of lisp & Writing](https://dreamsongs.com/ArtOfLisp.html)
;;   - [The why of Y](https://dreamsongs.com/Files/WhyOfY.pdf)
;;   - His writings on Christopher Alexander's Design Patterns are inspirational too! (Though not relevant to this talk)
;; - Paul Graham [Beating the averages](http://paulgraham.com/avg.html)
;;   - [... other Lisp essays](http://paulgraham.com/articles.html)
;; - [GNU Project](https://www.gnu.org/gnu/initial-announcement.html)
;; - [The development of GNU Emacs](https://www.gnu.org/gnu/rms-lisp.html)

;; ---
;; # Ok, so what is Clojure then?
;;
;; - A modern functional programming language
;; - Is a dialect of Lisp (but not a direct descendent of any prior Lisp)
;; - Created in 2007
;; - Open source (EPL license)
;; - Compiles to Java and Runs on the JVM
;; - Dynamically typed
;; - Created and led by Rich Hickey
;; - Has a strong development philosophy behind it

;; ---
;; # What else is Clojure?
;;
;; - ... a small but vibrant open-source community
;;   - I recommend the [Clojurian Slack](http://clojurians.net/)
;;   - Extremely welcoming (our female developers say great things about it too!)
;; - a dialect of similar "hosted languages"
;;    - [Clojure](https://clojure.org/) (official)
;;    - [Clojurescript](https://clojurescript.org/) (official)
;;    - [Small Clojure Interpreter](https://github.com/babashka/sci) & [Babashka](https://babashka.org/)
;;    - [ClojureCLR](https://github.com/clojure/clojure-clr)
;;    - [Clojure Dart](https://github.com/Tensegritics/ClojureDart) (new)

;; ---
;; # What else is Clojure?

;; ---
;; # Clojure is a boring language

;; ---
;; # Clojure is a boring language
;;
;; _* boring enough that you could possibly use it at work_
;;
;; _** about a dozen of us do!_

;; ---
;; # The best of boring
;;
;; - Most of Clojure Core Team work for Nubank
;;   - Worth > $30bn (now one of the biggest banks in Brazil)
;; - Nubank have built their business on Clojure (thousands of systems)
;;   - Acquired the core team because they rely heavily on Clojure & [Datomic](https://www.datomic.com/)
;; - It's reliable and predictable
;; - Host language (java) interop is great so there are usable libraries available for almost everything

;; ---
;; # The best of boring
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
;; ![Clojure Code retention](./resources/clojure-code-retention.png)

;; ---
;; ![Scala Code retention](./resources/scala-code-retention.png)

;; ---
;; # Stuff we've built in Clojure

;; ---
;; # [Energy Performance Cerficates](https://epc.opendatacommunities.org/)

;; EPC certificates for every house & business/premise in England & Wales

^{::clerk/visibility {:code :hide}}
(ImageIO/read (io/file "./resources/epcs.png"))

;; ---
;; # Indicies of Multiple Deprivation apps
;; Downloading by area or by postcode
;;
^{::clerk/visibility {:code :hide}}
(ImageIO/read (io/file "./resources/postcode.png"))


;; ---
;; # Publish My Data (v3)
;;
;; Atlas, Drafter, Grafter-Server

^{::clerk/visibility {:code :hide}}
(ImageIO/read (io/file "./resources/scotland.png"))

;; ---
;; # Drafter
^{::clerk/visibility {:code :hide}}
(ImageIO/read (io/file "./resources/drafter.png"))

;; ---
;; # Publish My Data (v4)
;; (Clojure & Clojurescript)

^{::clerk/visibility {:code :hide}}
(ImageIO/read (io/file "./resources/geo-picker.png"))

;; ---
;; # Environment Agency Asset Information System
;; And various other systems for EA

^{::clerk/visibility {:code :hide}}
(ImageIO/read (io/file "./resources/aims.png"))

;; ---
;; # [Matcha](https://github.com/Swirrl/matcha)
;; An in memory graph database (Clojure library)

^{::clerk/visibility {:code :hide}}
(ImageIO/read (io/file "./resources/matcha.png"))

;; ---
;; # [csv2rdf](https://github.com/Swirrl/csv2rdf)
;; Command Line tool implementing the W3C [CSV on the Web standards](https://www.w3.org/TR/2015/REC-csv2rdf-20151217/)
;;
;; Thanks Lee Kitching!!

^{::clerk/visibility {:code :hide}}
(ImageIO/read (io/file "./resources/csv2rdf.png"))

;; ---
;;
;; # The cool stuff;
;; # Clojure is contrarian, interesting and exciting

;; ---
;; # Big ideas in Clojure
;;
;; 1. ✅ Simplicity/Decomplected Design
;; 2. ✅ Lisp
;; 3. ✅ The best bits of boring
;; 4. Functional Programming
;; 5. The value of values.  Immutability changes everything!
;; 6. ~~Open extensibility~~
;; 7. Programming as a dialogue; working inside your code ; inside out programming
;; 8. Hosted language

;; ---
;; # What is Functional Programming?
;; - Programming with functions
;; - Functions vs procedures (purity)
^{::clerk/visibility {:code :hide}}
(clerk/tex "inc(x) = x + 1")
^{::clerk/visibility {:code :hide}}
(clerk/tex "inc(1) = 1 + 1")
^{::clerk/visibility {:code :hide}}
(clerk/tex "= 1 + 1")
^{::clerk/visibility {:code :hide}}
(clerk/tex "= 2")


;; ---
;; # What is Functional Programming?
;;
;; - Purity means programming with...
;; - An absence of side effects
;; - An absence of mutable state
;; - Simplicity!  Nothing can change underneath you!!
;; - No PLOP (Place Oriented Programming)
;; - Amazing for concurrent/parallel programming
;; - Amazing for stress reduction


;; ---
;; # Referential transparency
;;
;; > An expression is called referentially transparent if it can be replaced with its corresponding value (and vice-versa) without changing the program's behavior.[1] This requires that the expression be pure – its value must be the same for the same inputs and its evaluation must have no side effects.
;;
;; Allows you to refactor fearlessly.
;;
;; No quantum mechanics!  No spooky action at a distance!
;;
;; Ability to arbitrarily compose.


;; ---
;; # The value of values
;;
;; What is a value?
;;
;; Something that doesn't change, Immutable, and they evaluate to themselves.
;;
;; If I take `42`, and add `1` to it, `42` remains unchanged.

^{::clerk/visibility {:code :hide}}
(clerk/example 42
               (+ 42 1)
               42)

;; Yep!  It's still `42`! 😅

;; ---
;; # The value of values
;;
;; Other primitive values...
^{::clerk/visibility {:code :hide}}
(clerk/example true
               false
               :keywords
               :namespaced/keyword
               :another.namespaced/keyword
               1/3

               0.25
               'name.spaced/symbols
               'symbols)

;; ---
;; # The value of values
;;
;; What is a value?
;;
;; ... most other things in clojure


^{::clerk/visibility {:code :hide}}

(clerk/example [:vectors :are :like :arrays]
               '(:lists :are :values :too)
               {:hash "maps"
                :key-1 1
                :key-2 2}
               #{:sets :deduplicate})

;; Almost everything is immutable though, so can't be changed!

^{::clerk/visibility {:code :hide}}
(clerk/with-viewer editor-sync-viewer
  "(def my-data {:some {:data :here}})

(assoc my-data :more :data)

;;my-data
")

;; ---
;; # Immutability changes everything
;;
;; State of the art Persistent Data Structures (P Bagwell + innovations by Rich Hickey)
;;
;; (Later copied in Scala, Haskell & Erlang)
;; [HOPL History of Clojure Paper (p12)](https://clojure.org/about/history) [Video Lecture](https://clojure.org/about/history)
;;

^{::clerk/visibility {:code :hide}}
(clerk/with-viewer mermaid-viewer {:nextjournal.clerk/width :full}
  "flowchart LR
    Alice{Alice} -.-> 1
    Bob{Bob} -.-> head
    head(0) --> 1
    1 --> 2
    2 --> 3
    3 --> 4
")

^{::clerk/visibility {:code :hide}}
(clerk/with-viewer editor-sync-viewer
  "(def the-data '(1 2 3 4))

(def alice the-data)

alice

;(def bob (cons 0 the-data))

;bob

;; what does alice see?

;alice

")

;; ---
;; # Your code can be a value too!

^{::clerk/visibility {:code :hide}}
(clerk/with-viewer editor-sync-viewer
  "(def my-code '(+ 10 20))

my-code

;(eval my-code)

; (eval (cons * (rest my-code)))
")

;; ---
;; # Immutability changes everything
;;
;; [This paper](https://www.cidrdb.org/cidr2015/Papers/CIDR15_Paper16.pdf) discusses how the concept of immutability can drive improvements at many levels of the stack including applications.
;; e.g. non-destructive photo editing, docker, infrastructure as code...
^{::clerk/visibility {:code :hide}}
(ImageIO/read (io/file "./resources/immutability-changes-everything.png"))


;; ---
;; # Programming as a dialogue
;;
;; Switch to editor!!

;; ---
;; # Programming as a dialogue
;; Working inside your code
;; - Bottom up or Top down
;; - Rapid prototyping
;; - Fast feedback
;; - What about TDD?
;;   - But in Clojure we RDD our TDD!

#_(defn baz [c d]
  (+ c d))

#_(defn foo [a b]
  (let [c (+ a b)
        d (* c c)]
    ;; scope capture
    #_(sc.api/spy)
    (baz c d)))

#_(foo 10 20)


;; ---
;; # Laziness & Infinite Sequences
;;

(range)

(iterate inc 0)

(take 10 (iterate inc 0))

(repeat 100 :thing)

(map str (range))

(map-indexed vector (line-seq (io/reader "./notebooks/clojure_presentation.clj")))

;; ---
;; # Lazy seq caching
;;
;; A neat solution to making mutable resources like I/O streams appear immutable:
^{::clerk/visibility {:code :hide}}
(clerk/code "(defn line-seq
\"Returns the lines of text from rdr as a lazy sequence of strings.\"
 [^java.io.BufferedReader rdr]
(when-let [line (.readLine rdr)]
    (cons line (lazy-seq (line-seq rdr)))))")

;; Point to the head of an (abstract) sequence of lines in a file...

(def pres-lines (line-seq (io/reader "./notebooks/clojure_presentation.clj")))

(count pres-lines)

(first pres-lines)


(defn spreadsheet-columns
  [alphabet]
  (->> (map str alphabet)
       (iterate (fn [chars]
                  (for [x chars
                        y alphabet]
                    (str x y))))
       (mapcat identity)))

 (spreadsheet-columns "abcdefghijklmnopqrstuvwxyz")

;; ---
;; # Programming as a dialogue
;;
;; Switch to editor!!

;; ---
;; # Programming as a dialogue
;; Working inside your code
;; - Bottom up or Top down
;; - Rapid prototyping
;; - Fast feedback
;; - What about TDD?
;;   - But in Clojure we RDD our TDD!


#_(defn baz [c d]
  (+ c d))

#_(defn foo [a b]
  (let [c (+ a b)
        d (* c c)]
    ;; scope capture
    (sc.api/spy)
    (baz c d)))

#_(foo 10 20)

;; ---
;; # Deep Dive!
;; - Clojure bootstraping


;; ---
;; # Hosted language
;;
;; Clojure and it's dialects are hosted languages, and expect to
;; interoperate with a host language.
;;
;; In the case of:
;;
;; - Clojure -> JVM (including GraalVM)
;; - Clojurescript -> Javascript
;; - ClojureDart -> Dart (& Flutter)
;; - SCI -> cljc (clojure/clojurescript)
;; - Babashka compiles SCI with GraalVM interop


;; ---
;; # What could I use Clojure for?
;;
;; - Almost everything
;; - General purpose language
;; - It's pretty fast too (compiled, but feels like it's interpreted)
;; - Constrained mainly by it's host languages/runtimes (Java, Javascript, GraalVM, Dart)
;; - Constrained mainly by your willingness to use it

;; ---
;; # But other languages have functions and a REPL too!
;;
;; - Mutable state makes those REPLs significantly less useful
;; - Value semantics & Equality are a big deal here too!
;;   - Deep copies/comparisons always work (and are always intuitive)
;; - These properties mean you can build your program from inside it

;; ---
;; # Some cool Databases made in Clojure
;;
;; - Datomic (a triplestore but with transaction time)
;; - XTDB (like Datomic but bitemporal)
;; - Other Datalog's
;;   - TPXImpact's [Matcha](https://github.com/Swirrl/matcha)
;;   - [Asami](https://github.com/quoll/asami)
;;   - [Datascript](https://github.com/tonsky/datascript)
;;   - ...

;; ---
;; # Data science
;;
;; - A promising area for Clojure
;; - [Clerk notebooks](https://github.com/nextjournal/clerk)
;; - Language truly embraces Data
;;
;; - See Kira's talk at Clojure Conj!!!
^{::clerk/visibility {:code :hide}}
(clerk/html "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/MguatDl5u2Q\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>")

;; ---
;; # Books / Resources
;;
;; - [Getting Clojure](https://pragprog.com/titles/roclojure/getting-clojure/)
;; - [The Joy of Clojure](https://joyofclojure.github.io/)

;; ---
;; # Free / Resources
;; - [Clojure for the Brave and True](https://www.braveclojure.com)
;; - [clojure.org](https://clojure.org/)
;;   - ![Clojure Reference](https://clojure.org/reference)
;;   - ![Clojure Guides](https://clojure.org/guides)


;; ---
;; # 🙇 Thank you!
;;

;; ---
;; # What next?
;;
;; - We're keen to promote functional programming and Clojure within TPX, if you're interested `#cop_tech_fp`.
