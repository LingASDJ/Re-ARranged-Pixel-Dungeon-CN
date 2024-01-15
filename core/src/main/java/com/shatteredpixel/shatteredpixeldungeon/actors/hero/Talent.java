/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.items.Item.updateQuickslot;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EnhancedRings;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.InfiniteBullet;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PhysicalEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ScrollEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WandEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.BulletItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.bow.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gloves;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.gun.Gun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

public enum Talent {

	//Warrior T1
	HEARTY_MEAL					(0,  0),
	VETERANS_INTUITION			(1,  0),
	TEST_SUBJECT				(2,  0),
	IRON_WILL					(3,  0),
	MAX_HEALTH					(4,  0),
	//Warrior T2
	IRON_STOMACH				(5,  0),
	LIQUID_WILLPOWER			(6,  0),
	RUNIC_TRANSFERENCE			(7,  0),
	LETHAL_MOMENTUM				(8,  0),
	IMPROVISED_PROJECTILES		(9,  0),
	PARRY						(10, 0),
	//Warrior T3
	HOLD_FAST					(11, 0, 3),
	STRONGMAN					(12, 0, 3),
	//Berserker T3
	ENDLESS_RAGE				(13, 0, 3),
	DEATHLESS_FURY				(14, 0, 3),
	ENRAGED_CATALYST			(15, 0, 3),
	LETHAL_RAGE					(16, 0, 3),
	MAX_RAGE					(17, 0, 3),
	ENDURANCE					(18, 0, 3),
	//Gladiator T3
	CLEAVE						(19, 0, 3),
	LETHAL_DEFENSE				(20, 0, 3),
	ENHANCED_COMBO				(21, 0, 3),
	LIGHT_WEAPON				(22, 0, 3),
	OFFENSIVE_DEFENSE			(23, 0, 3),
	SKILL_REPEAT				(24, 0, 3),
	//Veteran T3
	POWERFUL_TACKLE				(25, 0, 3),
	MYSTICAL_TACKLE				(26, 0, 3),
	DELAYED_GRENADE				(27, 0, 3),
	INCAPACITATION				(28, 0, 3),
	SUPER_ARMOR					(29, 0, 3),
	IMPROVED_TACKLE				(30, 0, 3),
	//Heroic Leap T4
	BODY_SLAM					(31, 0, 4),
	IMPACT_WAVE					(32, 0, 4),
	DOUBLE_JUMP					(33, 0, 4),
	//Shockwave T4
	EXPANDING_WAVE				(34, 0, 4),
	STRIKING_WAVE				(35, 0, 4),
	SHOCK_FORCE					(36, 0, 4),
	//Endure T4
	SUSTAINED_RETRIBUTION		(37, 0, 4),
	SHRUG_IT_OFF				(38, 0, 4),
	EVEN_THE_ODDS				(39, 0, 4),

	//Mage T1
	EMPOWERING_MEAL				(0,  1),
	SCHOLARS_INTUITION			(1,  1),
	TESTED_HYPOTHESIS			(2,  1),
	BACKUP_BARRIER				(3,  1),
	CHARGE_PRESERVE				(4,  1),
	//Mage T2
	ENERGIZING_MEAL				(5,  1),
	INSCRIBED_POWER				(6,  1),
	WAND_PRESERVATION			(7,  1),
	ARCANE_VISION				(8,  1),
	SHIELD_BATTERY				(9,  1),
	FASTER_CHARGER				(10, 1),
	//Mage T3
	DESPERATE_POWER				(11, 1, 3),
	ALLY_WARP					(12, 1, 3),
	//Battlemage T3
	EMPOWERED_STRIKE			(13, 1, 3),
	MYSTICAL_CHARGE				(14, 1, 3),
	EXCESS_CHARGE				(15, 1, 3),
	BATTLE_MAGIC				(16, 1, 3),
	MAGIC_RUSH					(17, 1, 3),
	MAGICAL_CIRCLE				(18, 1, 3),
	//Warlock T3
	SOUL_EATER					(19, 1, 3),
	SOUL_SIPHON					(20, 1, 3),
	NECROMANCERS_MINIONS		(21, 1, 3),
	MADNESS						(22, 1, 3),
	ENHANCED_MARK				(23, 1, 3),
	MARK_OF_WEAKNESS			(24, 1, 3),
	//Wizard T3
	SPELL_ENHANCE				(25, 1, 3),
	BRIG_BOOST					(26, 1, 3),
	ENERGY_REMAINS				(27, 1, 3),
	MAGIC_EMPOWER				(28, 1, 3),
	SECOND_EFFECT				(29, 1, 3),
	LIFE_ENERGY					(30, 1, 3),
	//Elemental Blast T4
	BLAST_RADIUS				(31, 1, 4),
	ELEMENTAL_POWER				(32, 1, 4),
	REACTIVE_BARRIER			(33, 1, 4),
	//Wild Magic T4
	WILD_POWER					(34, 1, 4),
	FIRE_EVERYTHING				(35, 1, 4),
	CONSERVED_MAGIC				(36, 1, 4),
	//Warp Beacon T4
	TELEFRAG					(37, 1, 4),
	REMOTE_BEACON				(38, 1, 4),
	LONGRANGE_WARP				(39, 1, 4),

	//Rogue T1
	CACHED_RATIONS				(0,  2),
	THIEFS_INTUITION			(1,  2),
	SUCKER_PUNCH				(2,  2),
	PROTECTIVE_SHADOWS			(3,  2),
	EMERGENCY_ESCAPE			(4,  2),
	//Rogue T2
	MYSTICAL_MEAL				(5,  2),
	INSCRIBED_STEALTH			(6,  2),
	WIDE_SEARCH					(7,  2),
	SILENT_STEPS				(8,  2),
	ROGUES_FORESIGHT			(9,  2),
	MOVESPEED_ENHANCE			(10, 2),
	//Rogue T3
	ENHANCED_RINGS				(11, 2, 3),
	LIGHT_CLOAK					(12, 2, 3),
	//Assassin T3
	ENHANCED_LETHALITY			(13, 2, 3),
	ASSASSINS_REACH				(14, 2, 3),
	BOUNTY_HUNTER				(15, 2, 3),
	ENERGY_DRAW					(16, 2, 3),
	PERFECT_ASSASSIN			(17, 2, 3),
	CAUTIOUS_PREP				(18, 2, 3),
	//Freerunner T3
	EVASIVE_ARMOR				(19, 2, 3),
	PROJECTILE_MOMENTUM			(20, 2, 3),
	SPEEDY_STEALTH				(21, 2, 3),
	QUICK_PREP					(22, 2, 3),
	OVERCOMING					(23, 2, 3),
	MOMENTARY_FOCUSING			(24, 2, 3),
	//Chaser T3
	POISONOUS_BLADE				(25, 2, 3),
	LETHAL_SURPRISE				(26, 2, 3),
	CHAIN_CLOCK					(27, 2, 3),
	SOUL_COLLECT				(28, 2, 3),
	TRAIL_TRACKING				(29, 2, 3),
	MASTER_OF_CLOAKING			(30, 2, 3),
	//Smoke Bomb T4
	HASTY_RETREAT				(31, 2, 4),
	BODY_REPLACEMENT			(32, 2, 4),
	SHADOW_STEP					(33, 2, 4),
	//Death Mark T4
	FEAR_THE_REAPER				(34, 2, 4),
	DEATHLY_DURABILITY			(35, 2, 4),
	DOUBLE_MARK					(36, 2, 4),
	//Shadow Clone T4
	SHADOW_BLADE				(37, 2, 4),
	CLONED_ARMOR				(38, 2, 4),
	PERFECT_COPY				(39, 2, 4),

	//Huntress T1
	NATURES_BOUNTY				(0,  3),
	SURVIVALISTS_INTUITION		(1,  3),
	FOLLOWUP_STRIKE				(2,  3),
	NATURES_AID					(3,  3),
	WATER_FRIENDLY				(4,  3),
	//Huntress T2
	INVIGORATING_MEAL			(5,  3),
	LIQUID_NATURE				(6,  3),
	REJUVENATING_STEPS			(7,  3),
	HEIGHTENED_SENSES			(8,  3),
	DURABLE_PROJECTILES			(9,  3),
	ADDED_MEAL					(10, 3),
	//Huntress T3
	POINT_BLANK					(11, 3, 3),
	SEER_SHOT					(12, 3, 3),
	//Sniper T3
	FARSIGHT					(13, 3, 3),
	SHARED_ENCHANTMENT			(14, 3, 3),
	SHARED_UPGRADES				(15, 3, 3),
	KICK						(16, 3, 3),
	SHOOTING_EYES				(17, 3, 3),
	TARGET_SPOTTING				(18, 3, 3),
	//Warden T3
	DURABLE_TIPS				(19, 3, 3),
	BARKSKIN					(20, 3, 3),
	SHIELDING_DEW				(21, 3, 3),
	LIVING_GRASS				(22, 3, 3),
	ATTRACTION					(23, 3, 3),
	HEALING_DEW					(24, 3, 3),
	//Fighter T3
	SWIFT_MOVEMENT				(25, 3, 3),
	LESS_RESIST					(26, 3, 3),
	RING_KNUCKLE				(27, 3, 3),
	MYSTICAL_PUNCH				(28, 3, 3),
	QUICK_STEP					(29, 3, 3),
	COUNTER_ATTACK				(30, 3, 3),
	//Spectral Blades T4
	FAN_OF_BLADES				(31, 3, 4),
	PROJECTING_BLADES			(32, 3, 4),
	SPIRIT_BLADES				(33, 3, 4),
	//Natures Power T4
	GROWING_POWER				(34, 3, 4),
	NATURES_WRATH				(35, 3, 4),
	WILD_MOMENTUM				(36, 3, 4),
	//Spirit Hawk T4
	EAGLE_EYE					(37, 3, 4),
	GO_FOR_THE_EYES				(38, 3, 4),
	SWIFT_SPIRIT				(39, 3, 4),

	//Duelist T1
	STRENGTHENING_MEAL			(0,  4),
	ADVENTURERS_INTUITION		(1,  4),
	PATIENT_STRIKE				(2,  4),
	AGGRESSIVE_BARRIER			(3,  4),
	SKILLED_HAND				(4,  4),
	//Duelist T2
	FOCUSED_MEAL				(5,  4),
	LIQUID_AGILITY				(6,  4),
	WEAPON_RECHARGING			(7,  4),
	LETHAL_HASTE				(8,  4),
	SWIFT_EQUIP					(9,  4),
	ACCUMULATION				(10, 4),
	//Duelist T3
	PRECISE_ASSAULT				(11, 4, 3),
	DEADLY_FOLLOWUP				(12, 4, 3),
	//Champion T3
	SECONDARY_CHARGE			(13, 4, 3),
	TWIN_UPGRADES				(14, 4, 3),
	COMBINED_LETHALITY			(15, 4, 3),
	FASTER_CHARGE				(16, 4, 3),
	QUICK_FOLLOWUP				(17, 4, 3),
	TWIN_SWORD					(18, 4, 3),
	//Monk T3
	UNENCUMBERED_SPIRIT			(19, 4, 3),
	MONASTIC_VIGOR				(20, 4, 3),
	COMBINED_ENERGY				(21, 4, 3),
	RESTORED_ENERGY				(22, 4, 3),
	ENERGY_BARRIER				(23, 4, 3),
	HARMONY						(24, 4, 3),
	//Fencer T3
	CLAM_STEPS					(25, 4, 3),
	CRITICAL_MOMENTUM			(26, 4, 3),
	KINETIC_MOVEMENT			(27, 4, 3),
	AGGRESSIVE_MOVEMENT			(28, 4, 3),
	UNENCUMBERED_MOVEMENT		(29, 4, 3),
	SOULIZE						(30, 4, 3),
	//Challenge T4
	CLOSE_THE_GAP				(31, 4, 4),
	INVIGORATING_VICTORY		(32, 4, 4),
	ELIMINATION_MATCH			(33, 4, 4),
	//Elemental Strike T4
	ELEMENTAL_REACH				(34, 4, 4),
	STRIKING_FORCE				(35, 4, 4),
	DIRECTED_POWER				(36, 4, 4),
	//Feint T4
	FEIGNED_RETREAT				(37, 4, 4),
	EXPOSE_WEAKNESS				(38, 4, 4),
	COUNTER_ABILITY				(39, 4, 4),



	//Gunner T1
	RELOADING_MEAL				(0,  6),	//식사 시 장착한 총기 재장전/1발 더 재장전
	GUNNERS_INTUITION			(1,  6),	//총기를 장착 시 감정/습득 시 저주 여부 감정
	SPEEDY_MOVE					(2,  6),	//적을 처음 공격하면 신속 2/3턴
	SAFE_RELOAD					(3,  6),	//재장전 시 3/5의 방어막을 얻음
	MIND_VISION					(4,  6),	//이동 시 1%/2% 확률로 1턴의 심안을 얻음
	//Gunner T2
	INFINITE_BULLET_MEAL		(5,  6),	//식사에 1턴만 소모하고, 식사 시 2/3턴의 무한 탄환을 얻음
	INSCRIBED_BULLET			(6,  6),	//주문서 사용 시 5/10개의 탄환을 얻음
	BULLET_SAVING				(7,  6),	//재장전 시 1/2개의 탄환을 추가로 장전함. 탄환을 추가로 소모하지 않음. 쿨타임 10턴
	CAMOUFLAGE					(8,  6),	//길게 자란 풀을 밟으면 2/3턴의 투명화를 얻음
	LARGER_MAGAZINE				(9,  6),	//총기의 최대 탄약 수 1/2 증가
	BULLET_COLLECT				(10, 6),	//적을 처치하면 1/2개의 탄환을 드랍함
	//Gunner T3
	STREET_BATTLE				(11, 6, 3),	//탄환이 플레이어 주변 반경 2/3/4타일의 벽을 관통함
	FAST_RELOAD					(12, 6, 3),	//재장전에 필요한 턴이 1/2/3턴 감소
	//Outlaw T3
	ROLLING						(13, 6, 3),	//총을 쏜 이후 1/2/3턴 이내에 움직일 경우 탄환을 1발 장전함
	PERFECT_FOCUSING			(14, 6, 3),	//죽음의 룰렛 지속 시간이 2/4/6턴 증가함
	HONORABLE_SHOT				(15, 6, 3),	//죽음의 룰렛 스택 3 이상에서, 적의 공격을 회피한 직후 탄환을 발사하면 13/27/40% 미만의 체력을 가진 적을 즉사시킴
	BULLET_TIME					(16, 6, 3),	//죽음의 룰렛 스택 6으로 적을 즉사시키면 시야 내의 모든 적을 4/8/12턴 동안 둔화시킴
	INEVITABLE_DEATH			(17, 6, 3),	//죽음의 룰렛 스택 6이 되면 다음 탄환 공격이 2배/3배/4배의 정확성을 얻음
	HEADSHOT					(18, 6, 3),	//탄환이 1%/2%/3% 확률로 적을 즉사시킴
	//Gunslinger T3
	QUICK_RELOAD				(19, 6, 3),	//이동 시 3%/6%/9% 확률로 탄환을 1발 장전함
	MOVING_SHOT					(20, 6, 3),	//이동 직후 투척 무기와 탄환의 명중률 감소량이 17%/33%/50% 감소
	ELEMENTAL_BULLET			(21, 6, 3),	//탄환을 전부 소모한 상태에서 재장전 시 각 17% 확률로 빙결탄/화염탄/전격탄을 장전함
	IMPROVISATION				(22, 6, 3),	//탄환을 전부 소모하면 8/16/24의 방어막을 얻음
	SOUL_BULLET					(23, 6, 3),	//적을 처치하면 1/2/3턴의 무한 탄환을 얻음
	LIGHT_MOVEMENT				(24, 6, 3),	//갑옷의 힘 요구 수치를 초과한 힘 3/2/1당 이동 속도가 5% 증가
	//Specialist T3
	STEALTH_MASTER				(25, 6, 3),	//총기 어그로 제거/적이 근처에 있어도 은폐 가능/은폐 중 허기 소모 X
	SKILLFUL_RUNNER				(26, 6, 3),	//은폐가 해제될 때 2/4/6턴의 신속을 얻음. 재사용 대기 시간 30턴
	STEALTH						(27, 6, 3),	//적에게 들킬 확률 1/2/3단계 감소
	INTO_THE_SHADOW				(28, 6, 3),	//은폐 시도 시 은폐 대신 3/6/9턴의 투명화를 얻음. 재사용 대기 시간 15턴
	RANGED_SNIPING				(29, 6, 3),	//적과의 거리 1타일 당 투척 무기의 피해량이 2.5%/5%/7.5%씩 증가
	TELESCOPE					(30, 6, 3),	//시야 범위 25%/50%/75% 증가
	//Riot T4
	HASTE_MOVE					(31, 6, 4),
	SHOT_CONCENTRATION			(32, 6, 4),
	ROUND_PRESERVE				(33, 6, 4),
	//ReinforcedArmor T4
	BAYONET						(34, 6, 4),
	TACTICAL_SIGHT				(35, 6, 4),
	PLATE_ADD					(36, 6, 4),
	//FirstAidKit T4
	ADDITIONAL_MEDS				(37, 6, 4),
	THERAPEUTIC_BANDAGE			(38, 6, 4),
	FASTER_HEALING				(39, 6, 4),

	//universal T4
	HEROIC_ENERGY				(43, 0, 4), //See icon() and title() for special logic for this one
	//Ratmogrify T4
	RATSISTANCE					(40, 12, 4),
	RATLOMACY					(41, 12, 4),
	RATFORCEMENTS				(42, 12, 4),
	//universal T3
	ATK_SPEED_ENHANCE			(0, 12, 4),
	ACC_ENHANCE					(1, 12, 4),
	EVA_ENHANCE					(2, 12, 4),
	BETTER_CHOICE				(3, 12, 3);

	public static final int TALENT_NUMBER = 44;

	//warrior talent's buff
	//2-4
	public static class LethalMomentumTracker extends FlavourBuff{};
	//2-5
	public static class ImprovisedProjectileCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.15f, 0.2f, 0.5f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 50); }
	};
	//Berserker
	public static class MaxRageCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0xFF3333); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 50); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
	};
	//Shockwave 4-3
	public static class StrikingWaveTracker extends FlavourBuff{};



	//mage talent's buff
	//2-3
	public static class WandPreservationCounter extends CounterBuff{{revivePersists = true;}};
	//BattleMage 3-3
	public static class EmpoweredStrikeTracker extends FlavourBuff{};



	//rouge talent's buff
	//1-4
	public static class ProtectiveShadowsTracker extends Buff {
		float barrierInc = 0.5f;

		@Override
		public boolean act() {
			//barrier every 2/1 turns, to a max of 3/5
			if (((Hero)target).hasTalent(Talent.PROTECTIVE_SHADOWS) && target.invisible > 0){
				Barrier barrier = Buff.affect(target, Barrier.class);
				if (barrier.shielding() < 1 + 2*((Hero)target).pointsInTalent(Talent.PROTECTIVE_SHADOWS)) {
					barrierInc += 0.5f * ((Hero) target).pointsInTalent(Talent.PROTECTIVE_SHADOWS);
				}
				if (barrierInc >= 1){
					barrierInc = 0;
					barrier.incShield(1);
				} else {
					barrier.incShield(0); //resets barrier decay
				}
			} else {
				detach();
			}
			spend( TICK );
			return true;
		}

		private static final String BARRIER_INC = "barrier_inc";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put( BARRIER_INC, barrierInc);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			barrierInc = bundle.getFloat( BARRIER_INC );
		}
	}
	//Assassin 3-5
	public static class BountyHunterTracker extends FlavourBuff{};
	//Chaser
	public static class ChaseCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.4f, 0.4f, 0.8f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / (15)); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
		public void spendTime() { spend(-1f); }
	};
	//Chaser 3-4
	public static class LethalCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.8f, 0.1f, 0.1f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 5); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
		public void spendTime() { spend(-1f); }
	};
	//Chaser 3-5
	public static class ChainCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.2f, 0.5f, 0.8f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 10); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
		public void spendTime() { spend(-1f); }
	};


	//huntress talent's buff
	//2-3
	public static class RejuvenatingStepsCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0f, 0.35f, 0.15f); }
		public float iconFadePercent() { return GameMath.gate(0, visualcooldown() / (15 - 5*Dungeon.hero.pointsInTalent(REJUVENATING_STEPS)), 1); }
	};
	public static class RejuvenatingStepsFurrow extends CounterBuff{{revivePersists = true;}};
	//3-2
	public static class SeerShotCooldown extends FlavourBuff{
		public int icon() { return target.buff(RevealedArea.class) != null ? BuffIndicator.NONE : BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.7f, 0.4f, 0.7f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 20); }
	};
	//Spectral Blades 4-3
	public static class SpiritBladesTracker extends FlavourBuff{};
	//Sniper
	public static class KickCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0xF27318); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 10); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
	};
	public static class ShootingEyesTracker extends Buff{};
	//Fighter
	public static class QuickStep extends FlavourBuff {

		{
			type = buffType.NEUTRAL;
			announced = false;
		}

		public static final float DURATION	= 1f;

		@Override
		public int icon() {
			return BuffIndicator.HASTE;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0x2364BC);
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION - visualcooldown()) / DURATION);
		}

		@Override
		public String toString() {
			return Messages.get(this, "name");
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", dispTurns());
		}
	}
	//Fighter 3-8
	public static class CounterAttackTracker extends Buff{};


	//duelist talent's buff
	//1-3
	public static class PatientStrikeTracker extends Buff {
		public int pos;
		{ type = Buff.buffType.POSITIVE; }
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.5f, 0f, 1f); }
		@Override
		public boolean act() {
			if (pos != target.pos) {
				detach();
			} else {
				spend(TICK);
			}
			return true;
		}
		private static final String POS = "pos";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(POS, pos);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			pos = bundle.getInt(POS);
		}
	};
	//1-4
	public static class AggressiveBarrierCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.35f, 0f, 0.7f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 50); }
	};
	//2-2
	public static class RestoredAgilityTracker extends FlavourBuff{};
	//2-4
	public static class LethalHasteCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.35f, 0f, 0.7f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 100); }
	};
	//2-5
	public static class SwiftEquipCooldown extends FlavourBuff{
		public boolean secondUse;
		public boolean hasSecondUse(){
			return secondUse && cooldown() > 14f;
		}

		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) {
			if (hasSecondUse()) icon.hardlight(0.85f, 0f, 1.0f);
			else                icon.hardlight(0.35f, 0f, 0.7f);
		}
		public float iconFadePercent() { return GameMath.gate(0, visualcooldown() / 20f, 1); }

		private static final String SECOND_USE = "second_use";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(SECOND_USE, secondUse);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			secondUse = bundle.getBoolean(SECOND_USE);
		}
	};
	//2-6
	public static class SkilledHandTracker extends Buff{};
	//3-1
	public static class PreciseAssaultTracker extends FlavourBuff{
		{ type = buffType.POSITIVE; }
		public int icon() { return BuffIndicator.INVERT_MARK; }
		public void tintIcon(Image icon) { icon.hardlight(1f, 1f, 0.0f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
	};
	//3-2
	public static class DeadlyFollowupTracker extends FlavourBuff{
		public int object;
		{ type = Buff.buffType.POSITIVE; }
		public int icon() { return BuffIndicator.INVERT_MARK; }
		public void tintIcon(Image icon) { icon.hardlight(0.5f, 0f, 1f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
		private static final String OBJECT    = "object";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(OBJECT, object);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			object = bundle.getInt(OBJECT);
		}
	}
	//Champion 3-5
	public static class CombinedLethalityAbilityTracker extends FlavourBuff{
		public MeleeWeapon weapon;
	};
	public static class CombinedLethalityTriggerTracker extends FlavourBuff{
		{ type = buffType.POSITIVE; }
		public int icon() { return BuffIndicator.CORRUPT; }
		public void tintIcon(Image icon) { icon.hardlight(0.6f, 0.15f, 0.6f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
	};
	//Monk 3-5
	public static class CombinedEnergyAbilityTracker extends FlavourBuff{
		public int energySpent = -1;
		public boolean wepAbilUsed = false;
	}
	public static class AgressiveMovementAbilityTracker extends FlavourBuff{
		public boolean wepAbilUsed = false;
	}
	public static class QuickFollowupCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0x651F66); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 10); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
	};
	public static class QuickFollowupTracker extends FlavourBuff{};

	//Feint 4-3
	public static class CounterAbilityTacker extends FlavourBuff{};

	//gunner talent's buff
	public static class SpeedyMoveTracker extends Buff{};
	public static class BulletSavingCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0x666666); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 10); }
	};
	public static class RollingTracker extends FlavourBuff{};
	public static class HonorableShotTracker extends FlavourBuff{};
	public static class SkillfulRunnerCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight( 0xFFFF00 ); }
		public float iconFadePercent() { return Math.max(0, 1-visualcooldown() / 30); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
	};
	public static class IntoTheShadowCooldown extends FlavourBuff{
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight( 0x7FA9D2 ); }
		public float iconFadePercent() { return Math.max(0, 1-visualcooldown() / 15); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
	};


	int icon;
	int maxPoints;

	// tiers 1/2/3/4 start at levels 2/7/13/21
	public static int[] tierLevelThresholds = new int[]{0, 2, 7, 13, 21, 31};

	Talent( int x, int y ){
		this(x, y, 2);
	}

	Talent( int x, int y, int maxPoints ){
		this.icon = x+TALENT_NUMBER*y;
		this.maxPoints = maxPoints;
	}

	public int icon(){
		if (this == HEROIC_ENERGY){
			int x = 43;
			int y = 0;
			HeroClass cls = Dungeon.hero != null ? Dungeon.hero.heroClass : GamesInProgress.selectedClass;
			switch (cls){
				case WARRIOR: default:
					y = 0;
					break;
				case MAGE:
					y = 1;
					break;
				case ROGUE:
					y = 2;
					break;
				case HUNTRESS:
					y = 3;
					break;
				case DUELIST:
					y = 4;
					break;
				case GUNNER:
					y = 6;
					break;
			}
			if (Ratmogrify.useRatroicEnergy){
				y = 12;
			}
			return x+TALENT_NUMBER*y;
		} else {
			return icon;
		}
	}

	public int maxPoints(){
		return maxPoints;
	}

	public String title(){
		if (this == HEROIC_ENERGY && Ratmogrify.useRatroicEnergy){
			return Messages.get(this, name() + ".rat_title");
		}
		return Messages.get(this, name() + ".title");
	}

	public final String desc(){
		return desc(false);
	}

	public String desc(boolean metamorphed){
		if (metamorphed){
			String metaDesc = Messages.get(this, name() + ".meta_desc");
			if (!metaDesc.equals(Messages.NO_TEXT_FOUND)){
				return Messages.get(this, name() + ".desc") + "\n\n" + metaDesc;
			}
		}
		return Messages.get(this, name() + ".desc");
	}

	public static void onTalentUpgraded( Hero hero, Talent talent ){
		//universal
		if (talent == BETTER_CHOICE){
			switch (hero.pointsInTalent(Talent.BETTER_CHOICE)) {
				case 0: default:
					break;
				case 1:
					StoneOfEnchantment stone = new StoneOfEnchantment();
					if (stone.doPickUp( Dungeon.hero )) {
						GLog.i( Messages.get(Dungeon.hero, "you_now_have", stone.name() ));
						hero.spend(-1);
					} else {
						Dungeon.level.drop( stone, Dungeon.hero.pos ).sprite.drop();
					}
					break;
				case 2:
					ScrollOfEnchantment enchantment = new ScrollOfEnchantment();
					if (enchantment.doPickUp( Dungeon.hero )) {
						GLog.i( Messages.get(Dungeon.hero, "you_now_have", enchantment.name() ));
						hero.spend(-1);
					} else {
						Dungeon.level.drop( enchantment, Dungeon.hero.pos ).sprite.drop();
					}
					break;
				case 3:
					ScrollOfUpgrade scl = new ScrollOfUpgrade();
					if (scl.doPickUp( Dungeon.hero )) {
						GLog.i( Messages.get(Dungeon.hero, "you_now_have", scl.name() ));
						hero.spend(-1);
					} else {
						Dungeon.level.drop( scl, Dungeon.hero.pos ).sprite.drop();
					}
					break;
			}
		}

		//warrior
		//for metamorphosis
		if (talent == IRON_WILL && hero.heroClass != HeroClass.WARRIOR){
			Buff.affect(hero, BrokenSeal.WarriorShield.class);
		}

		if (talent == VETERANS_INTUITION && hero.pointsInTalent(VETERANS_INTUITION) == 2){
			if (hero.belongings.armor() != null)  hero.belongings.armor.identify();
		}

		if (talent == MAX_HEALTH) {
			hero.updateHT(true);
		}

		if (talent == PARRY) {
			Buff.affect(hero, ParryTracker.class);
		}



		//rouge
		if (talent == THIEFS_INTUITION && hero.pointsInTalent(THIEFS_INTUITION) == 2){
			if (hero.belongings.ring instanceof Ring) hero.belongings.ring.identify();
			if (hero.belongings.misc instanceof Ring) hero.belongings.misc.identify();
			for (Item item : Dungeon.hero.belongings){
				if (item instanceof Ring){
					((Ring) item).setKnown();
				}
			}
		}
		if (talent == THIEFS_INTUITION && hero.pointsInTalent(THIEFS_INTUITION) == 1){
			if (hero.belongings.ring instanceof Ring) hero.belongings.ring.setKnown();
			if (hero.belongings.misc instanceof Ring) ((Ring) hero.belongings.misc).setKnown();
		}

		if (talent == PROTECTIVE_SHADOWS && hero.invisible > 0){
			Buff.affect(hero, Talent.ProtectiveShadowsTracker.class);
		}

		if (talent == LIGHT_CLOAK && hero.heroClass == HeroClass.ROGUE){
			for (Item item : Dungeon.hero.belongings.backpack){
				if (item instanceof CloakOfShadows){
					if (hero.buff(LostInventory.class) == null || item.keptThroughLostInventory()) {
						((CloakOfShadows) item).activate(Dungeon.hero);
					}
				}
			}
		}



		//huntress
		if (talent == ADVENTURERS_INTUITION && hero.pointsInTalent(ADVENTURERS_INTUITION) == 2){
			if (hero.belongings.weapon() != null) hero.belongings.weapon().identify();
		}

		if (talent == HEIGHTENED_SENSES || talent == FARSIGHT || talent == TELESCOPE){
			Dungeon.observe();
		}



		//duelist
		if (talent == ACCUMULATION) {
			updateQuickslot();
		}

		if (talent == SECONDARY_CHARGE || talent == TWIN_UPGRADES || talent == DESPERATE_POWER){
			Item.updateQuickslot();
		}

		if (talent == UNENCUMBERED_SPIRIT && hero.pointsInTalent(talent) == 3){
			Item toGive = new ClothArmor().identify();
			if (!toGive.collect()){
				Dungeon.level.drop(toGive, hero.pos).sprite.drop();
			}
			toGive = new Gloves().identify();
			if (!toGive.collect()){
				Dungeon.level.drop(toGive, hero.pos).sprite.drop();
			}
		}


		//gunner
		if (talent == GUNNERS_INTUITION && hero.belongings.weapon instanceof Gun){
			hero.belongings.weapon.identify();
		}

		if (talent == LARGER_MAGAZINE) {
			Item.updateQuickslot();
		}
	}

	public static class CachedRationsDropped extends CounterBuff{{revivePersists = true;}};
	public static class NatureBerriesAvailable extends CounterBuff{{revivePersists = true;}}; //for pre-1.3.0 saves
	public static class NatureBerriesDropped extends CounterBuff{{revivePersists = true;}};

	public static void onFoodEaten( Hero hero, float foodVal, Item foodSource ){
		if (hero.hasTalent(HEARTY_MEAL)){
			//3/5 HP healed, when hero is below 25% health
			if (hero.HP <= hero.HT/4) {
				hero.HP = Math.min(hero.HP + 1 + 2 * hero.pointsInTalent(HEARTY_MEAL), hero.HT);
				hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1+hero.pointsInTalent(HEARTY_MEAL));
			//2/3 HP healed, when hero is below 50% health
			} else if (hero.HP <= hero.HT/2){
				hero.HP = Math.min(hero.HP + 1 + hero.pointsInTalent(HEARTY_MEAL), hero.HT);
				hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), hero.pointsInTalent(HEARTY_MEAL));
			}
		}
		if (hero.hasTalent(IRON_STOMACH)){
			if (hero.cooldown() > 0) {
				Buff.affect(hero, WarriorFoodImmunity.class, hero.cooldown());
			}
		}
		if (hero.hasTalent(EMPOWERING_MEAL)){
			//2/3 bonus wand damage for next 3 zaps
			Buff.affect( hero, WandEmpower.class).set(1 + hero.pointsInTalent(EMPOWERING_MEAL), 3);
			ScrollOfRecharging.charge( hero );
		}
		if (hero.hasTalent(ENERGIZING_MEAL)){
			//5/8 turns of recharging
			Buff.prolong( hero, Recharging.class, 2 + 3*(hero.pointsInTalent(ENERGIZING_MEAL)) );
			ScrollOfRecharging.charge( hero );
			SpellSprite.show(hero, SpellSprite.CHARGE);
		}
		if (hero.hasTalent(MYSTICAL_MEAL)){
			//3/5 turns of recharging
			ArtifactRecharge buff = Buff.affect( hero, ArtifactRecharge.class);
			if (buff.left() < 1 + 2*(hero.pointsInTalent(MYSTICAL_MEAL))){
				Buff.affect( hero, ArtifactRecharge.class).set(1 + 2*(hero.pointsInTalent(MYSTICAL_MEAL))).ignoreHornOfPlenty = foodSource instanceof HornOfPlenty;
			}
			ScrollOfRecharging.charge( hero );
			SpellSprite.show(hero, SpellSprite.CHARGE, 0, 1, 1);
		}
		if (hero.hasTalent(INVIGORATING_MEAL)){
			//effectively 1/2 turns of haste
			Buff.prolong( hero, Haste.class, 0.67f+hero.pointsInTalent(INVIGORATING_MEAL));
		}
		if (hero.hasTalent(STRENGTHENING_MEAL)){
			//3 bonus physical damage for next 2/3 attacks
			Buff.affect( hero, PhysicalEmpower.class).set(3, 1 + hero.pointsInTalent(STRENGTHENING_MEAL));
		}
		if (hero.hasTalent(FOCUSED_MEAL)){
			if (hero.heroClass == HeroClass.DUELIST){
				//1/1.5 charge for the duelist
				Buff.affect( hero, MeleeWeapon.Charger.class ).gainCharge(0.5f*(hero.pointsInTalent(FOCUSED_MEAL)+1));
			} else {
				// lvl/3 / lvl/2 bonus dmg on next hit for other classes
				Buff.affect( hero, PhysicalEmpower.class).set(Math.round(hero.lvl / (4f - hero.pointsInTalent(FOCUSED_MEAL))), 1);
			}
		}
		if (hero.hasTalent(Talent.RELOADING_MEAL)) {
			if (hero.belongings.weapon instanceof Gun) {
				((Gun)hero.belongings.weapon).quickReload();
				if (hero.pointsInTalent(Talent.RELOADING_MEAL) > 1) {
					((Gun)hero.belongings.weapon).manualReload(1, true);
				}
			}
		}
		if (hero.hasTalent(Talent.INFINITE_BULLET_MEAL)) {
			Buff.affect(hero, InfiniteBullet.class, 1+hero.pointsInTalent(Talent.INFINITE_BULLET_MEAL));
		}
	}

	public static class WarriorFoodImmunity extends FlavourBuff{
		{ actPriority = HERO_PRIO+1; }
	}

	public static float itemIDSpeedFactor( Hero hero, Item item ){
		// 1.75x/2.5x speed with Huntress talent
		float factor = 1f + 0.75f*hero.pointsInTalent(SURVIVALISTS_INTUITION);

		// Affected by both Warrior(1.75x/2.5x) and Duelist(2.5x/inst.) talents
		if (item instanceof MeleeWeapon){
			factor *= 1f + 1.5f*hero.pointsInTalent(ADVENTURERS_INTUITION); //instant at +2 (see onItemEquipped)
			factor *= 1f + 0.75f*hero.pointsInTalent(VETERANS_INTUITION);
		}
		// Affected by both Warrior(2.5x/inst.) and Duelist(1.75x/2.5x) talents
		if (item instanceof Armor){
			factor *= 1f + 0.75f*hero.pointsInTalent(ADVENTURERS_INTUITION);
			factor *= 1f + hero.pointsInTalent(VETERANS_INTUITION); //instant at +2 (see onItemEquipped)
		}
		// 3x/instant for Mage (see Wand.wandUsed())
		if (item instanceof Wand){
			factor *= 1f + 2.0f*hero.pointsInTalent(SCHOLARS_INTUITION);
		}
		// 2x/instant for Rogue (see onItemEqupped), also id's type on equip/on pickup
		if (item instanceof Ring){
			factor *= 1f + hero.pointsInTalent(THIEFS_INTUITION);
		}
		return factor;
	}

	public static void onPotionUsed( Hero hero, int cell, float factor ){
		if (hero.hasTalent(LIQUID_WILLPOWER)){
			if (hero.heroClass == HeroClass.WARRIOR) {
				BrokenSeal.WarriorShield shield = hero.buff(BrokenSeal.WarriorShield.class);
				if (shield != null) {
					// 50/75% of total shield
					int shieldToGive = Math.round(factor * shield.maxShield() * 0.25f * (1 + hero.pointsInTalent(LIQUID_WILLPOWER)));
					shield.supercharge(shieldToGive);
				}
			} else {
				// 5/7.5% of max HP
				int shieldToGive = Math.round( factor * hero.HT * (0.025f * (1+hero.pointsInTalent(LIQUID_WILLPOWER))));
				Buff.affect(hero, Barrier.class).setShield(shieldToGive);
			}
		}
		if (hero.hasTalent(LIQUID_NATURE)){
			ArrayList<Integer> grassCells = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS9){
				grassCells.add(cell+i);
			}
			Random.shuffle(grassCells);
			for (int grassCell : grassCells){
				Char ch = Actor.findChar(grassCell);
				if (ch != null && ch.alignment == Char.Alignment.ENEMY){
					//1/2 turns of roots
					Buff.affect(ch, Roots.class, factor * hero.pointsInTalent(LIQUID_NATURE));
				}
				if (Dungeon.level.map[grassCell] == Terrain.EMPTY ||
						Dungeon.level.map[grassCell] == Terrain.EMBERS ||
						Dungeon.level.map[grassCell] == Terrain.EMPTY_DECO){
					Level.set(grassCell, Terrain.GRASS);
					GameScene.updateMap(grassCell);
				}
				CellEmitter.get(grassCell).burst(LeafParticle.LEVEL_SPECIFIC, 4);
			}
			// 4/6 cells total
			int totalGrassCells = (int) (factor * (2 + 2 * hero.pointsInTalent(LIQUID_NATURE)));
			while (grassCells.size() > totalGrassCells){
				grassCells.remove(0);
			}
			for (int grassCell : grassCells){
				int t = Dungeon.level.map[grassCell];
				if ((t == Terrain.EMPTY || t == Terrain.EMPTY_DECO || t == Terrain.EMBERS
						|| t == Terrain.GRASS || t == Terrain.FURROWED_GRASS)
						&& Dungeon.level.plants.get(grassCell) == null){
					Level.set(grassCell, Terrain.HIGH_GRASS);
					GameScene.updateMap(grassCell);
				}
			}
			Dungeon.observe();
		}
		if (hero.hasTalent(LIQUID_AGILITY)){
			Buff.prolong(hero, RestoredAgilityTracker.class, hero.cooldown() + Math.max(0, factor-1));
		}
	}

	public static void onScrollUsed( Hero hero, int pos, float factor ){
		if (hero.hasTalent(INSCRIBED_POWER)){
			// 2/3 empowered wand zaps
			Buff.affect(hero, ScrollEmpower.class).reset((int) (factor * (1 + hero.pointsInTalent(INSCRIBED_POWER))));
		}
		if (hero.hasTalent(INSCRIBED_STEALTH)){
			// 3/5 turns of stealth
			Buff.affect(hero, Invisibility.class, factor * (1 + 2*hero.pointsInTalent(INSCRIBED_STEALTH)));
			Sample.INSTANCE.play( Assets.Sounds.MELD );
		}
		if (hero.hasTalent(Talent.MAGIC_RUSH)) {
			MagesStaff staff = hero.belongings.getItem(MagesStaff.class);
			if (staff != null) {
				staff.gainCharge(hero.pointsInTalent(Talent.MAGIC_RUSH), false);
				ScrollOfRecharging.charge(hero);
			}
		}
		if (hero.hasTalent(Talent.INSCRIBED_BULLET)) {
			//collects 5/10 bullet
			BulletItem bulletItem = new BulletItem();
			bulletItem.quantity(5*hero.pointsInTalent(Talent.INSCRIBED_BULLET));
			bulletItem.doPickUp(hero);
		}
	}

	public static void onUpgradeScrollUsed( Hero hero ){
		if (hero.hasTalent(INSCRIBED_POWER)){
			if (hero.heroClass == HeroClass.MAGE) {
				MagesStaff staff = hero.belongings.getItem(MagesStaff.class);
				if (staff != null) {
					staff.gainCharge(2 + 2 * hero.pointsInTalent(INSCRIBED_POWER), true);
					ScrollOfRecharging.charge(Dungeon.hero);
					SpellSprite.show(hero, SpellSprite.CHARGE);
				}
			} else {
				Buff.affect(hero, Recharging.class, 4 + 8 * hero.pointsInTalent(INSCRIBED_POWER));
			}
		}
	}

	public static void onArtifactUsed( Hero hero ){
		if (hero.hasTalent(ENHANCED_RINGS)){
			Buff.prolong(hero, EnhancedRings.class, 3f*hero.pointsInTalent(ENHANCED_RINGS));
		}
	}

	public static void onItemEquipped( Hero hero, Item item ){
		if (hero.pointsInTalent(VETERANS_INTUITION) == 2 && item instanceof Armor){
			item.identify();
		}
		if (hero.hasTalent(THIEFS_INTUITION) && item instanceof Ring){
			if (hero.pointsInTalent(THIEFS_INTUITION) == 2){
				item.identify();
			} else {
				((Ring) item).setKnown();
			}
		}
		if (hero.pointsInTalent(ADVENTURERS_INTUITION) == 2 && item instanceof Weapon){
			item.identify();
		}
		if (hero.hasTalent(GUNNERS_INTUITION) && item instanceof Gun) {
			item.identify();
		}
	}

	public static void onItemCollected( Hero hero, Item item ){
		if (hero.pointsInTalent(THIEFS_INTUITION) == 2){
			if (item instanceof Ring) ((Ring) item).setKnown();
		}
		if (hero.pointsInTalent(GUNNERS_INTUITION) == 2 && item instanceof Gun) {
			item.cursedKnown = true;
		}
	}

	//note that IDing can happen in alchemy scene, so be careful with VFX here
	public static void onItemIdentified( Hero hero, Item item ){
		if (hero.hasTalent(TEST_SUBJECT)){
			//heal for 2/3 HP
			hero.HP = Math.min(hero.HP + 1 + hero.pointsInTalent(TEST_SUBJECT), hero.HT);
			if (hero.sprite != null) {
				Emitter e = hero.sprite.emitter();
				if (e != null) e.burst(Speck.factory(Speck.HEALING), hero.pointsInTalent(TEST_SUBJECT));
			}
		}
		if (hero.hasTalent(TESTED_HYPOTHESIS)){
			//2/3 turns of wand recharging
			Buff.affect(hero, Recharging.class, 1f + hero.pointsInTalent(TESTED_HYPOTHESIS));
			ScrollOfRecharging.charge(hero);
		}
	}

	public static int onAttackProc( Hero hero, Char enemy, int dmg ){
		if (hero.hasTalent(Talent.SUCKER_PUNCH)
				&& enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)
				&& enemy.buff(SuckerPunchTracker.class) == null){
			dmg += Random.IntRange(hero.pointsInTalent(Talent.SUCKER_PUNCH) , 2);
			Buff.affect(enemy, SuckerPunchTracker.class);
		}

		if (hero.hasTalent(Talent.FOLLOWUP_STRIKE) && enemy.isAlive() && enemy.alignment == Char.Alignment.ENEMY) {
			if (hero.belongings.attackingWeapon() instanceof MissileWeapon) {
				Buff.prolong(hero, FollowupStrikeTracker.class, 5f).object = enemy.id();
			} else if (hero.buff(FollowupStrikeTracker.class) != null
					&& hero.buff(FollowupStrikeTracker.class).object == enemy.id()){
				dmg += 1 + hero.pointsInTalent(FOLLOWUP_STRIKE);
				if (hero.belongings.weapon == null && hero.subClass == HeroSubClass.FIGHTER) {
					Buff.affect( enemy, Paralysis.class, 1f );
				}
				hero.buff(FollowupStrikeTracker.class).detach();
			}
		}

		if (hero.buff(Talent.SpiritBladesTracker.class) != null
				&& Random.Int(10) < 3*hero.pointsInTalent(Talent.SPIRIT_BLADES)){
			SpiritBow bow = hero.belongings.getItem(SpiritBow.class);
			if (bow != null) dmg = bow.proc( hero, enemy, dmg );
			hero.buff(Talent.SpiritBladesTracker.class).detach();
		}

		if (hero.hasTalent(PATIENT_STRIKE)){
			if (hero.buff(PatientStrikeTracker.class) != null
					&& !(hero.belongings.attackingWeapon() instanceof MissileWeapon)){
				hero.buff(PatientStrikeTracker.class).detach();
				dmg += Random.IntRange(hero.pointsInTalent(Talent.PATIENT_STRIKE), 2);
			}
		}

		if (hero.hasTalent(DEADLY_FOLLOWUP) && enemy.alignment == Char.Alignment.ENEMY) {
			if (hero.belongings.attackingWeapon() instanceof MissileWeapon) {
				if (!(hero.belongings.attackingWeapon() instanceof SpiritBow.SpiritArrow)) {
					Buff.prolong(hero, DeadlyFollowupTracker.class, 5f).object = enemy.id();
				}
			} else if (hero.buff(DeadlyFollowupTracker.class) != null
					&& hero.buff(DeadlyFollowupTracker.class).object == enemy.id()){
				dmg = Math.round(dmg * (1.0f + .08f*hero.pointsInTalent(DEADLY_FOLLOWUP)));
			}
		}

		if (hero.hasTalent(Talent.SPEEDY_MOVE) && enemy instanceof Mob && enemy.buff(SpeedyMoveTracker.class) == null){
			Buff.affect(enemy, SpeedyMoveTracker.class);
			Buff.prolong(hero, Haste.class, 1f + hero.pointsInTalent(Talent.SPEEDY_MOVE));
		}

		return dmg;
	}

	public static class ParryCooldown extends Buff{
		float cooldown;
		public void set() {
			cooldown = 90-20*hero.pointsInTalent(Talent.PARRY)+1;
		}
		@Override
		public boolean act() {
			cooldown -= Actor.TICK;
			if (cooldown <= 0) {
				Buff.affect(target, ParryTracker.class);
				detach();
			}
			if (!hero.hasTalent(Talent.PARRY)) {
				detach();
			}
			spend(Actor.TICK);
			return true;
		}
	};

	public static class ParryTracker extends Buff{
		{ actPriority = HERO_PRIO+1;}

		{
			type = buffType.NEUTRAL;
			announced = true;
		}

		@Override
		public int icon() {
			return BuffIndicator.PARRY;
		}

		@Override
		public boolean act() {
			if (!hero.hasTalent(Talent.PARRY)) {
				detach();
			}
			spend(Actor.TICK);
			return true;
		}

		@Override
		public void detach() {
			super.detach();
			Buff.affect(target, ParryCooldown.class).set();
		}
	};
	public static class RiposteTracker extends Buff{
		{ actPriority = VFX_PRIO;}

		public Char enemy;

		@Override
		public boolean act() {
			target.sprite.attack(enemy.pos, new Callback() {
				@Override
				public void call() {
					AttackIndicator.target(enemy);
					if (hero.attack(enemy, 1f, 0, 1)){
						Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
					}

					next();
				}
			});
			detach();
			return false;
		}
	}
	public static class SuckerPunchTracker extends Buff{};
	public static class FollowupStrikeTracker extends FlavourBuff{
		public int object;
		{ type = Buff.buffType.POSITIVE; }
		public int icon() { return BuffIndicator.INVERT_MARK; }
		public void tintIcon(Image icon) { icon.hardlight(0f, 0.75f, 1f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
		private static final String OBJECT    = "object";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(OBJECT, object);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			object = bundle.getInt(OBJECT);
		}
	};

	public static final int MAX_TALENT_TIERS = 4;

	public static void initClassTalents( Hero hero ){
		initClassTalents( hero.heroClass, hero.talents, hero.metamorphedTalents );
	}

	public static void initClassTalents( HeroClass cls, ArrayList<LinkedHashMap<Talent, Integer>> talents){
		initClassTalents( cls, talents, new LinkedHashMap<>());
	}

	public static void initClassTalents( HeroClass cls, ArrayList<LinkedHashMap<Talent, Integer>> talents, LinkedHashMap<Talent, Talent> replacements ){
		while (talents.size() < MAX_TALENT_TIERS){
			talents.add(new LinkedHashMap<>());
		}

		ArrayList<Talent> tierTalents = new ArrayList<>();

		//tier 1
		switch (cls){
			case WARRIOR: default:
				Collections.addAll(tierTalents, HEARTY_MEAL, VETERANS_INTUITION, TEST_SUBJECT, IRON_WILL, MAX_HEALTH);
				break;
			case MAGE:
				Collections.addAll(tierTalents, EMPOWERING_MEAL, SCHOLARS_INTUITION, TESTED_HYPOTHESIS, BACKUP_BARRIER, CHARGE_PRESERVE);
				break;
			case ROGUE:
				Collections.addAll(tierTalents, CACHED_RATIONS, THIEFS_INTUITION, SUCKER_PUNCH, PROTECTIVE_SHADOWS, EMERGENCY_ESCAPE);
				break;
			case HUNTRESS:
				Collections.addAll(tierTalents, NATURES_BOUNTY, SURVIVALISTS_INTUITION, FOLLOWUP_STRIKE, NATURES_AID, WATER_FRIENDLY);
				break;
			case DUELIST:
				Collections.addAll(tierTalents, STRENGTHENING_MEAL, ADVENTURERS_INTUITION, PATIENT_STRIKE, AGGRESSIVE_BARRIER, SKILLED_HAND);
				break;
			case GUNNER:
				Collections.addAll(tierTalents, RELOADING_MEAL, GUNNERS_INTUITION, SPEEDY_MOVE, SAFE_RELOAD, MIND_VISION);
				break;
		}
		for (Talent talent : tierTalents){
			if (replacements.containsKey(talent)){
				talent = replacements.get(talent);
			}
			talents.get(0).put(talent, 0);
		}
		tierTalents.clear();

		//tier 2
		switch (cls){
			case WARRIOR: default:
				Collections.addAll(tierTalents, IRON_STOMACH, LIQUID_WILLPOWER, RUNIC_TRANSFERENCE, LETHAL_MOMENTUM, IMPROVISED_PROJECTILES, PARRY);
				break;
			case MAGE:
				Collections.addAll(tierTalents, ENERGIZING_MEAL, INSCRIBED_POWER, WAND_PRESERVATION, ARCANE_VISION, SHIELD_BATTERY, FASTER_CHARGER);
				break;
			case ROGUE:
				Collections.addAll(tierTalents, MYSTICAL_MEAL, INSCRIBED_STEALTH, WIDE_SEARCH, SILENT_STEPS, ROGUES_FORESIGHT, MOVESPEED_ENHANCE);
				break;
			case HUNTRESS:
				Collections.addAll(tierTalents, INVIGORATING_MEAL, LIQUID_NATURE, REJUVENATING_STEPS, HEIGHTENED_SENSES, DURABLE_PROJECTILES, ADDED_MEAL);
				break;
			case DUELIST:
				Collections.addAll(tierTalents, FOCUSED_MEAL, LIQUID_AGILITY, WEAPON_RECHARGING, LETHAL_HASTE, SWIFT_EQUIP, ACCUMULATION);
				break;
			case GUNNER:
				Collections.addAll(tierTalents, INFINITE_BULLET_MEAL, INSCRIBED_BULLET, BULLET_SAVING, CAMOUFLAGE, LARGER_MAGAZINE, BULLET_COLLECT);
				break;
		}
		for (Talent talent : tierTalents){
			if (replacements.containsKey(talent)){
				talent = replacements.get(talent);
			}
			talents.get(1).put(talent, 0);
		}
		tierTalents.clear();

		//tier 3
		switch (cls){
			case WARRIOR: default:
				Collections.addAll(tierTalents, HOLD_FAST, STRONGMAN);
				break;
			case MAGE:
				Collections.addAll(tierTalents, DESPERATE_POWER, ALLY_WARP);
				break;
			case ROGUE:
				Collections.addAll(tierTalents, ENHANCED_RINGS, LIGHT_CLOAK);
				break;
			case HUNTRESS:
				Collections.addAll(tierTalents, POINT_BLANK, SEER_SHOT);
				break;
			case DUELIST:
				Collections.addAll(tierTalents, PRECISE_ASSAULT, DEADLY_FOLLOWUP);
				break;
			case GUNNER:
				Collections.addAll(tierTalents, STREET_BATTLE, FAST_RELOAD);
				break;
		}
		for (Talent talent : tierTalents){
			if (replacements.containsKey(talent)){
				talent = replacements.get(talent);
			}
			talents.get(2).put(talent, 0);
		}
		tierTalents.clear();

		//tier4
		//TBD
	}

	public static void initSubclassTalents( Hero hero ){
		initSubclassTalents( hero.subClass, hero.talents );
	}

	public static void initSubclassTalents( HeroSubClass cls, ArrayList<LinkedHashMap<Talent, Integer>> talents ){
		if (cls == HeroSubClass.NONE) return;

		while (talents.size() < MAX_TALENT_TIERS){
			talents.add(new LinkedHashMap<>());
		}

		ArrayList<Talent> tierTalents = new ArrayList<>();

		Collections.addAll(tierTalents, ATK_SPEED_ENHANCE, ACC_ENHANCE, EVA_ENHANCE, BETTER_CHOICE );
		//tier 3
		switch (cls){
			case BERSERKER: default:
				Collections.addAll(tierTalents, ENDLESS_RAGE, DEATHLESS_FURY, ENRAGED_CATALYST, LETHAL_RAGE, MAX_RAGE, ENDURANCE );
				break;
			case GLADIATOR:
				Collections.addAll(tierTalents, CLEAVE, LETHAL_DEFENSE, ENHANCED_COMBO, LIGHT_WEAPON, OFFENSIVE_DEFENSE, SKILL_REPEAT );
				break;
			case VETERAN:
				Collections.addAll(tierTalents, POWERFUL_TACKLE, MYSTICAL_TACKLE, DELAYED_GRENADE, INCAPACITATION, SUPER_ARMOR, IMPROVED_TACKLE	);
				break;
			case BATTLEMAGE:
				Collections.addAll(tierTalents, EMPOWERED_STRIKE, MYSTICAL_CHARGE, EXCESS_CHARGE, BATTLE_MAGIC, MAGIC_RUSH, MAGICAL_CIRCLE );
				break;
			case WARLOCK:
				Collections.addAll(tierTalents, SOUL_EATER, SOUL_SIPHON, NECROMANCERS_MINIONS, MADNESS, ENHANCED_MARK, MARK_OF_WEAKNESS );
				break;
			case WIZARD:
				Collections.addAll(tierTalents, SPELL_ENHANCE, BRIG_BOOST, ENERGY_REMAINS, MAGIC_EMPOWER, SECOND_EFFECT, LIFE_ENERGY );
				break;
			case ASSASSIN:
				Collections.addAll(tierTalents, ENHANCED_LETHALITY, ASSASSINS_REACH, BOUNTY_HUNTER, ENERGY_DRAW, PERFECT_ASSASSIN, CAUTIOUS_PREP );
				break;
			case FREERUNNER:
				Collections.addAll(tierTalents, EVASIVE_ARMOR, PROJECTILE_MOMENTUM, SPEEDY_STEALTH, QUICK_PREP, OVERCOMING, MOMENTARY_FOCUSING );
				break;
			case CHASER:
				Collections.addAll(tierTalents, POISONOUS_BLADE, LETHAL_SURPRISE, CHAIN_CLOCK, SOUL_COLLECT, TRAIL_TRACKING, MASTER_OF_CLOAKING );
				break;
			case SNIPER:
				Collections.addAll(tierTalents, FARSIGHT, SHARED_ENCHANTMENT, SHARED_UPGRADES, KICK, SHOOTING_EYES, TARGET_SPOTTING );
				break;
			case WARDEN:
				Collections.addAll(tierTalents, DURABLE_TIPS, BARKSKIN, SHIELDING_DEW, LIVING_GRASS, ATTRACTION, HEALING_DEW );
				break;
			case FIGHTER:
				Collections.addAll(tierTalents, SWIFT_MOVEMENT, LESS_RESIST, RING_KNUCKLE, MYSTICAL_PUNCH, QUICK_STEP, COUNTER_ATTACK );
				break;
			case CHAMPION:
				Collections.addAll(tierTalents, SECONDARY_CHARGE, TWIN_UPGRADES, COMBINED_LETHALITY, FASTER_CHARGE, QUICK_FOLLOWUP, TWIN_SWORD );
				break;
			case MONK:
				Collections.addAll(tierTalents, UNENCUMBERED_SPIRIT, MONASTIC_VIGOR, COMBINED_ENERGY, RESTORED_ENERGY, ENERGY_BARRIER, HARMONY );
				break;
			case FENCER:
				Collections.addAll(tierTalents, CLAM_STEPS, CRITICAL_MOMENTUM, KINETIC_MOVEMENT, AGGRESSIVE_MOVEMENT, UNENCUMBERED_MOVEMENT, SOULIZE );
				break;
			case OUTLAW:
				Collections.addAll(tierTalents, ROLLING, PERFECT_FOCUSING, HONORABLE_SHOT, BULLET_TIME, INEVITABLE_DEATH, HEADSHOT );
				break;
			case GUNSLINGER:
				Collections.addAll(tierTalents, QUICK_RELOAD, MOVING_SHOT, ELEMENTAL_BULLET, IMPROVISATION, SOUL_BULLET, LIGHT_MOVEMENT );
				break;
			case SPECIALIST:
				Collections.addAll(tierTalents, STEALTH_MASTER, SKILLFUL_RUNNER, STEALTH, INTO_THE_SHADOW, RANGED_SNIPING, TELESCOPE );
				break;
		}
		for (Talent talent : tierTalents){
			talents.get(2).put(talent, 0);
		}
		tierTalents.clear();

	}

	public static void initArmorTalents( Hero hero ){
		initArmorTalents( hero.armorAbility, hero.talents);
	}

	public static void initArmorTalents(ArmorAbility abil, ArrayList<LinkedHashMap<Talent, Integer>> talents ){
		if (abil == null) return;

		while (talents.size() < MAX_TALENT_TIERS){
			talents.add(new LinkedHashMap<>());
		}

		for (Talent t : abil.talents()){
			talents.get(3).put(t, 0);
		}
	}

	private static final String TALENT_TIER = "talents_tier_";

	public static void storeTalentsInBundle( Bundle bundle, Hero hero ){
		for (int i = 0; i < MAX_TALENT_TIERS; i++){
			LinkedHashMap<Talent, Integer> tier = hero.talents.get(i);
			Bundle tierBundle = new Bundle();

			for (Talent talent : tier.keySet()){
				if (tier.get(talent) > 0){
					tierBundle.put(talent.name(), tier.get(talent));
				}
				if (tierBundle.contains(talent.name())){
					tier.put(talent, Math.min(tierBundle.getInt(talent.name()), talent.maxPoints()));
				}
			}
			bundle.put(TALENT_TIER+(i+1), tierBundle);
		}

		Bundle replacementsBundle = new Bundle();
		for (Talent t : hero.metamorphedTalents.keySet()){
			replacementsBundle.put(t.name(), hero.metamorphedTalents.get(t));
		}
		bundle.put("replacements", replacementsBundle);
	}

	private static final HashSet<String> removedTalents = new HashSet<>();
	static{
		//v2.2.0
		removedTalents.add("EMPOWERING_SCROLLS");
		//v1.4.0
		removedTalents.add("BERSERKING_STAMINA");
	}

	private static final HashMap<String, String> renamedTalents = new HashMap<>();
	static{
		//v2.2.0
		renamedTalents.put("RESTORED_WILLPOWER",        "LIQUID_WILLPOWER");
		renamedTalents.put("ENERGIZING_UPGRADE",        "INSCRIBED_POWER");
		renamedTalents.put("MYSTICAL_UPGRADE",          "INSCRIBED_STEALTH");
		renamedTalents.put("RESTORED_NATURE",           "LIQUID_NATURE");
		renamedTalents.put("RESTORED_AGILITY",          "LIQUID_AGILITY");
		//v2.1.0
		renamedTalents.put("LIGHTWEIGHT_CHARGE",        "PRECISE_ASSAULT");
		//v2.0.0 BETA
		renamedTalents.put("LIGHTLY_ARMED",             "UNENCUMBERED_SPIRIT");
		//v2.0.0
		renamedTalents.put("ARMSMASTERS_INTUITION",     "VETERANS_INTUITION");
	}

	public static void restoreTalentsFromBundle( Bundle bundle, Hero hero ){
		if (bundle.contains("replacements")){
			Bundle replacements = bundle.getBundle("replacements");
			for (String key : replacements.getKeys()){
				String value = replacements.getString(key);
				if (renamedTalents.containsKey(key)) key = renamedTalents.get(key);
				if (renamedTalents.containsKey(value)) value = renamedTalents.get(value);
				if (!removedTalents.contains(key) && !removedTalents.contains(value)){
					try {
						hero.metamorphedTalents.put(Talent.valueOf(key), Talent.valueOf(value));
					} catch (Exception e) {
						ShatteredPixelDungeon.reportException(e);
					}
				}
			}
		}

		if (hero.heroClass != null)     initClassTalents(hero);
		if (hero.subClass != null)      initSubclassTalents(hero);
		if (hero.armorAbility != null)  initArmorTalents(hero);

		for (int i = 0; i < MAX_TALENT_TIERS; i++){
			LinkedHashMap<Talent, Integer> tier = hero.talents.get(i);
			Bundle tierBundle = bundle.contains(TALENT_TIER+(i+1)) ? bundle.getBundle(TALENT_TIER+(i+1)) : null;

			if (tierBundle != null){
				for (String tName : tierBundle.getKeys()){
					int points = tierBundle.getInt(tName);
					if (renamedTalents.containsKey(tName)) tName = renamedTalents.get(tName);
					if (!removedTalents.contains(tName)) {
						try {
							Talent talent = Talent.valueOf(tName);
							if (tier.containsKey(talent)) {
								tier.put(talent, Math.min(points, talent.maxPoints()));
							}
						} catch (Exception e) {
							ShatteredPixelDungeon.reportException(e);
						}
					}
				}
			}
		}
	}

}
