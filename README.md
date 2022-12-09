# VelocityUtil

### The plugin adds RCON for the Velocity proxy and allows you to send commands to other servers using RCON, allowing you to synchronize commands between servers. Also, the plugin allows you to run php/bash scripts, in order to run php scripts on the hosting, PHP must be installed. You can turn off or turn on all the plug-in modules, so you can turn off functions you don't use and the plug-in won't load them when the server starts.

| Command | Description | Permission |
| --- | --- | --- |
| vureload | reload all configuration | velocityutil.reload |
| rcon [reload/all/server] [command] | send rcon command to server/servers | velocityutil.rcon.[all]/[reload]/[server_name] |
| php [script/reload] [args] | run php script (requires php installed on hosting) | velocityutil.php.[*]/[reload]/[script_name] |
| bash [script/reload] [args] | run bash script | velocityutil.bash.[*]/[reload]/[script_name] |


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
```

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
