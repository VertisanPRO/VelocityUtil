# VelocityUtil

## The plugin adds RCON for the Velocity proxy server and allows you to send commands to other servers using RCON.

| Command | Description |
| --- | --- |
| vureload | reload all configuration |
| rcon reload | reload rcon manager configuration |
| rcon [all/server] [command] | send rcon command to server/servers |

## config.yml
```yml
# Ceneral Configuration File

language: "en"

modules:
  # Rcon manager. Allows sending RCON commands to other servers
  rcon-manager: true
  # Rcon server. Enables Velocity RCON server to run
  rcon-server: true
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
