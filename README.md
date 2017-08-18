
# Block Protect Field Generator

A simple mod which add a block to protect a fixed area in a RP way for multiplayer servers, slightly inspired from the tool cupboard from Rust (also designed to work on a Davinci vessels).

## Install

Add the jar to `mods/` on the server and the client.

## Craft

![Craft](/doc/images/craft.png?raw=true)

## Usage

* mine the block with a Pickaxe (will reset registered players)
* right-click to register yourself to the generator
* sneak + right-click to show all registered players
* as OP, you can type `/bpfg allowme` to allow yourself on every generators blocking you in the area

## Characteristics

* each field generator protect a cube of radius 8 from the center (so 8+1+8 blocks on a specific axis)
* if only one hostile field generator is present, you will no be able to build, break or interact directly with blocks (but dropped items, xp, pressure plates, etc will still work)
* you can't place a generator if its field will intersect a hostile generator field
* the generator will be disabled if powered (redstone)
* the generator prevent explosions in and around the field (even when disabled)
* an indicator at the bottom right of the screen will be displayed when in the range of one or more generator fields
