// //This function is to create a block
// package com.example.SmartBuildingBackend.seed;

// import com.example.SmartBuildingBackend.entity.space.Space;
// import com.example.SmartBuildingBackend.entity.space.SpaceType;
// import com.example.SmartBuildingBackend.repository.space.SpaceRepository;
// import com.example.SmartBuildingBackend.repository.space.SpaceTypeRepository;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;

// import java.util.UUID;

// @Component
// public class SpaceSeeder implements CommandLineRunner {

//     private final SpaceRepository spaceRepository;
//     private final SpaceTypeRepository spaceTypeRepository;

//     public SpaceSeeder(SpaceRepository spaceRepository, SpaceTypeRepository spaceTypeRepository) {
//         this.spaceRepository = spaceRepository;
//         this.spaceTypeRepository = spaceTypeRepository;
//     }

//     @Override
//     public void run(String... args) {
//         UUID blockTypeId = UUID.fromString("e86e6e24-e15a-4995-ac2b-f18479441a26");
//         UUID floorTypeId = UUID.fromString("f4bc692b-e041-4e40-ba8f-e32f67f30cbf");
//         UUID roomTypeId = UUID.fromString("cf05f6ff-5e19-49b8-ae1c-04336aca641e");

//         SpaceType blockType = spaceTypeRepository.findById(blockTypeId).orElseThrow();
//         SpaceType floorType = spaceTypeRepository.findById(floorTypeId).orElseThrow();
//         SpaceType roomType = spaceTypeRepository.findById(roomTypeId).orElseThrow();

//         // Create Block
//         Space blockB8 = new Space();
//         blockB8.setSpaceName("8");
//         blockB8.setSpaceType(blockType);
//         blockB8.setParent(null);
//         blockB8 = spaceRepository.save(blockB8);

//         // 4 Floors
//         for (int floorNum = 1; floorNum <= 4; floorNum++) {
//             Space floor = new Space();
//             floor.setSpaceName(String.valueOf(floorNum));
//             floor.setSpaceType(floorType);
//             floor.setParent(blockB8);
//             floor = spaceRepository.save(floor);

//             // 23 Rooms
//             for (int roomNum = 1; roomNum <= 23; roomNum++) {
//                 String roomName = floorNum + String.format("%02d", roomNum); // 101, 102, ...

//                 Space room = new Space();
//                 room.setSpaceName(roomName);
//                 room.setSpaceType(roomType);
//                 room.setParent(floor);
//                 spaceRepository.save(room);
//             }
//         }

//         System.out.println("✅ Seeded Block B8 with 4 floors and 23 rooms per floor.");
//     }
// }
