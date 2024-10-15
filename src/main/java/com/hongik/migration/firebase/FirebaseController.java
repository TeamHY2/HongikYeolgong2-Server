//package com.hongik.migration.firebase;
//
//import io.swagger.v3.oas.annotations.Hidden;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//
//@Hidden
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/firebase")
//@RestController
//public class FirebaseController {
//
//    private final FirebaseService firebaseService;
//
//    @GetMapping
//    public ResponseEntity<Object> getMembers() throws ExecutionException, InterruptedException {
//        List<Member> list = firebaseService.getAllMembers();
//        return ResponseEntity.ok().body(list);
//    }
//
//    @GetMapping("/one")
//    public ResponseEntity<Object> getMember() throws ExecutionException, InterruptedException {
//        Member list = firebaseService.getMember("OhWV9CkaoranaSbKwbUQW5b5CsE2");
//        return ResponseEntity.ok().body(list);
//    }
//
//    @GetMapping("/study")
//    public ResponseEntity<Object> getStudy() throws ExecutionException, InterruptedException {
//        List<StudyDay> list = firebaseService.getAllStudyDays();
//        return ResponseEntity.ok().body(list);
//    }
//}
