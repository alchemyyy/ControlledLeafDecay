# Controlled Leaf Decay

Adds more control over leaf decay.

## What it does

- Speeds up leaf decay after trees are cut down.
- Keeps the behavior simple and configurable.
- Supports Fabric, Forge, and NeoForge from one multiloader project.

## Configuration

Edit `config/controlledleafdecay.json`:

```json
{
  "MinimumDecayTime": 8,
  "MaximumDecayTime": 32
}
```

Values are in ticks. Run `/cld reload` with permission level 2, or restart the game/server, after changing the file.

## Build

```powershell
.\gradlew.bat build
```

Loader jars are written under each loader module's `build/libs` directory.

## Credits

- Alchemyyy
- Discord: https://discord.gg/3TCfgHx7gv
