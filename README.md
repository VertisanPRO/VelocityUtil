# VelocityUtil

### You can turn off or turn on all the plug-in modules, so you can turn off functions you don't use and the plug-in won't load them when the server starts.

| Command | Description | Permission |
| --- | --- | --- |
| vureload | reload all configuration | velocityutil.reload |

## config.yml
```yml
# Ceneral Configuration File

language: "en"

modules:
  # Rcon manager. Allows sending RCON commands to other servers
  rcon-manager: true
  # Rcon server. Enables Velocity RCON server to run
  rcon-server: true
  # PHP Runner. Allows you to download the PHP script
  php-runner: true
  # BASH Runner. Allows you to download the BASH script
  bash-runner: true
  # Events manager. Allows you to use an event handler
  events-manager: true
```

### The module allows you to run php/bash scripts, in order to run php scripts on the hosting, PHP must be installed.

| Command | Description | Permission |
| --- | --- | --- |
| php [script/reload] [args] | run php script (requires php installed on hosting) | velocityutil.php.[*]/[reload]/[script_name] |
| bash [script/reload] [args] | run bash script | velocityutil.bash.[*]/[reload]/[script_name] |

### The module adds RCON for the Velocity proxy.

| Command | Description | Permission |
| --- | --- | --- |
| rcon [reload/all/server] [command] | send rcon command to server/servers | velocityutil.rcon.[all]/[reload]/[server_name] |

## rcon-manager.yml
```yml
# Rcon servers
# To allow the use of a separate server for a player, use permission:
# velocityutil.rcon.serve_name
# Examples: velocityutil.rcon.lobby, velocityutil.rcon.vanilla
servers:
  lobby:
    ip: "0.0.0.0"
    port: 25566
    pass: "rcon password"

  vanilla:
    ip: "0.0.0.0"
    port: 25566
    pass: "rcon password"
```

### The module allows you to send commands to other servers using RCON, allowing you to synchronize commands between servers. 
## rcon-server.yml
```yml
# Rcon server settings

# Rcon port
port: 25570
# Rcon password
password: "password"
# The response is colored or not
colored: true
```

### The event module allows you to run commands at various events
## events.yml
```yml
# Events settings
# Placeholders: {player}, {server}, {fromServer}
# [console] - run console command
# [delay] (seconds) - delay seconds command
events:
  on_join_commands:
    enabled: true
    commands:
      - "[console] alert &6Player {player} join the game"
      - "[delay] 10"
      - "server vanilla"
  on_leave_commands:
    enabled: true
    commands:
      - "[console] alert &6Player {player} left the game"
  on_server_switch:
    enabled: true
    commands:
      - "[console] alert &6Player {player} connected to server {server} from server {fromServer}"
  on_server_kick:
    enabled: true
    commands:
      - "[console] alert &6Player {player} kick the server {server}"
      - "[delay] 60"
      - "server {server}"
```
