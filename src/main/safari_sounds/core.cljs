(ns safari-sounds.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]

            ;; ↓ registers audio effects ↓
            [airsonic-ui.audio.core]))

;; events

(re-frame/reg-event-fx
 :play-sound
 (fn play-sound [_ [_ sound-url]]
   {:audio/play sound-url}))

(re-frame/reg-event-fx
 :pause-sound
 (fn pause-sound [_ _]
   {:audio/pause nil}))

(re-frame/reg-event-fx :audio/update (fn [_ _])) ;; ← no-op to stop warnings for unhandled event 

(defn dispatch [event & {:keys [sync?]}]
  (fn [e]
    (.preventDefault e)
    (if sync?
      (re-frame/dispatch-sync event)
      (re-frame/dispatch event))))

;; views

(defn player []
  (let [play [:play-sound "./100038__soundbytez__saz-birds-hyena.mp3"]
        pause [:pause-sound]]
    [:p
     [:span
      [:a {:href "#" :on-click (dispatch play)} "Play sound"]
      " "
      [:a {:href "#" :on-click (dispatch play :sync? true)} "Play sound (sync)"]]
     [:br]
     [:span
      [:a {:href "#" :on-click (dispatch pause)} "Pause sound"]
      " "
      [:a {:href "#" :on-click (dispatch pause :sync? true)} "Pause sound (sync)"]]]))

(defn main-view []
  [:div
   [:h1 "Safari Sounds"]
   [:p "This is a minimalistic example to reproduce the buggy audio behavior found in airsonic-ui as per commit a6b14d6. If the sound below is playing it should be fixed."]
   [player]
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
