(ns safari-sounds.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]

            ;; ↓ registers audio effects ↓
            [airsonic-ui.audio.core]))

;; events

(re-frame/reg-event-fx
 :play-sound
 (fn play-sound [coeffects [_ sound-url]]
   {:audio/play sound-url}))

;; views

(defn player []
  (let [event [:play-sound "./100038__soundbytez__saz-birds-hyena.mp3"]]
    [:span
     [:a {:href "#"
          :on-click (fn [e]
                      (.preventDefault e)
                      (re-frame/dispatch event))} "Play sound"]
     " "
     [:a {:href "#"
          :on-click (fn [e]
                      (.preventDefault e)
                      (re-frame/dispatch-sync event))} "Play sound (sync)"]]))

(defn main-view []
  [:div
   [:h1 "Safari Sounds"]
   [:p "This is a minimalistic example to reproduce the buggy audio behavior found in airsonic-ui as per commit a6b14d6. If the sound below is playing it should be fixed."]
   [:p [player]]
   [:p.smaller "Sound taken from freesound.org: "
    [:a {:href "https://freesound.org/people/soundbytez/sounds/100038/"
         :target "_blank"} "saz-birds-hyena by soundbytez"]]])

;; run it

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [main-view] (.getElementById js/document "app")))

(defn ^:export init []
  ;; (re-frame/dispatch-sync [::events/initialize-app])
  (enable-console-print!)
  (mount-root))
