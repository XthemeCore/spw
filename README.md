# spw
F2 Assignment

# 5610110364
XthemeCore

# Update

## *01*
- Add sprite graphic for spaceship and enemy via sprite class
- Add audio class
- Add sample background music via game engine class
- Resize spaceship and enemy to support sprite (32*32 pixels)

## *02*
- Add Smooth 8 Directions Movement
- Add Bullet class with sprite
- Add setAlive method to Enemy

## *03*
-Add full screen mode

## *04*
- Add Scrolling Background

## *05*
- Fix game panel position in full screen mode
- Add ESC key to exit program
- Add Counter to delay ammo when shoot

## *06*  เพิ่ม GUI, ระบบด่าน (Stage),เสียงกระสุนและระเบิด
- เพิ่มปุ่ม ESC สำหรับ pause เกมในขณะเล่น และสำหรับออกจากเกมในขณะที่อยู่หน้าต่างเมนู
- เพิ่มปืนกระสุนและระเบิด
- เพิ่มระบบ Stage โดยมีสูงสุด 10 Stage และ แต่ละ Stage จะมีกำหนดคะแนนสำหรับผ่าน Stage นั้น
- แก้ไขคะแนนเมื่อยิง Enemy เป็น 100 คูณ 2 ยกกำลัง Stage - 1 เพื่อให้มีความเป็นไปได้ในการชนะเกมมากขึ้น
- เพิ่ม GUI Title Screen, Pause Screen, StageUp Screen, Game Over Screen และ Win Screen
- ปรับปรุง class Audio ให้รับ Exception รวม, ตัด UpdatePlay ออก ใช้การตรวจสอบการวนซ้ำเพลงใน GameEngine แทน
- ยกเลิกการใช้งาน AlphaComposite ที่ทำให้ Enemy และ Bullet จางลง ถาวร
- จัด Code ให้สวยงามยิ่งขึ้น
