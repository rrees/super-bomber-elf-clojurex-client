(ns super-bomber-elf-client.core
  (:gen-class)
  (:require [clojure.data.json :as json]
      [gniazdo.core :as ws]))

(def connection-url "ws://178.62.75.118:80")

(def show-messages (atom false))

(defn messages [] (swap! show-messages (fn [a] (not a))))

(defn create-socket [] (ws/connect connection-url
  :on-receive (fn receive-message [message] (if @show-messages (println (json/read-str message))))))

(defn send-command [socket command]
    (ws/send-msg socket (json/write-str command)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (messages)
    (let [ socket (create-socket)]
        (send-command socket {"command" ["SetName" "Robert"]})
        (ws/close socket))
  )

;;{validCommands [{command DropBomb} {command [SetName <name>]} {command MoveNorth} {command MoveSouth} {command MoveEast} {command MoveWest}], hint Messages must be valid JSON, and plain strings aren't allowed as top-level JSON elements, so everything must be wrapped in a message object. Blame Doug Crockford, not me!}
