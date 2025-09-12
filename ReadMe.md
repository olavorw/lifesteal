# Lifesteal
*plugin by Olav "Olavorw" Sharma*

The code for this is super messy, but it somehow works. I will try to clean it up in the future. _maybe_

A comprehensive Minecraft Paper plugin that implements lifesteal mechanics where players gain and lose hearts through PvP combat.

## Features

### Core Mechanics
- **Heart Transfer System**: When Player A kills Player B, the victim loses 1 heart and the killer gains 1 heart
- **Health Boundaries**: Players can have between 1-20 hearts (2-40 HP)
- **Elimination System**: Players who reach 0 hearts are automatically banned until revived

### Heart Items
- **Consumable Hearts**: Special crafted items that can be consumed to gain +1 heart
- **Custom Recipe**: Craft hearts using expensive materials:
  ```
  [Netherite] [Diamond Block] [Netherite]
  [Gold Block] [ Nether Star ] [Gold Block]
  [Netherite] [Diamond Block] [Netherite]
  ```
- **Right-Click to Use**: Simple interaction to consume heart items
- **Visual & Audio Feedback**: Level-up sounds and colored chat messages

### Administrative Tools
- **Player Management**: Give/remove hearts, revive eliminated players
- **Server Administration**: Reset entire lifesteal system or individual players
- **Heart Item Management**: Generate heart items for players or events
- **Health Monitoring**: Check any player's current heart count

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/giveheartitem [player] [amount]` | Give heart items to a player | `lifesteal.admin` |
| `/giveheart [player] [amount]` | Directly add hearts to a player | `lifesteal.admin` |
| `/removeheart [player] [amount]` | Remove hearts from a player | `lifesteal.admin` |
| `/withdrawheart [player] [amount]` | Convert player hearts to heart items | `lifesteal.player.withdraw` |
| `/reviveplayer [player]` | Unban and restore eliminated player | `lifesteal.admin` |
| `/lifestealreset` | Reset entire server's lifesteal system | `lifesteal.admin` |
| `/resethearts [player]` | Reset player to default hearts (10) | `lifesteal.admin` |
| `/checkhealth [player]` | Check a player's current heart count | `lifesteal.admin` |

## Installation

### Requirements
- **Server Software**: Paper 1.21.5+ (Bukkit/Spigot compatible)
- **Java Version**: Java 21+

### Setup Instructions
1. Download the [latest release](https://github.com/olavorw/lifesteal/releases/latest) (latest minecraft version) or you can go to the [Releases](https://github.com/olavorw/lifesteal/releases) page to find one for the minecraft version you're looking for
2. Place the file it downloaded in your server's `plugins/` directory
3. Start/restart your server
4. The plugin will automatically generate default configuration

### First Time Setup
- All players start with **10 hearts** (20 HP) by default
- Heart recipe is automatically registered and available for crafting
- No additional configuration required - ready to play!

## How to Play

### For Players
1. **PvP Combat**: Kill other players to steal their hearts
2. **Craft Hearts**: Use the expensive recipe to create consumable heart items
3. **Manage Risk**: Avoid dying when you have few hearts remaining
4. **Strategic Withdrawing**: Convert your hearts to items for trading or storage

### For Server Administrators
- Use administrative commands to manage player hearts
- Monitor eliminated players and revive them as needed
- Reset systems during events or server wipes
- Generate heart items for rewards or events

## Permissions

```yaml
lifesteal.admin:
  description: Grants access to all administrative commands
  default: op

lifesteal.player.withdraw:
  description: Allows using /withdrawheart command
  default: true
```