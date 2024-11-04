//package com.hongik.migration.firebase;
//
//import com.google.api.core.ApiFuture;
//import com.google.cloud.Timestamp;
//import com.google.cloud.firestore.Firestore;
//import com.google.cloud.firestore.Query;
//import com.google.cloud.firestore.QueryDocumentSnapshot;
//import com.google.cloud.firestore.QuerySnapshot;
//import com.google.firebase.cloud.FirestoreClient;
//import com.hongik.domain.study.StudySession;
//import com.hongik.domain.study.StudySessionRepository;
//import com.hongik.domain.user.Role;
//import com.hongik.domain.user.User;
//import com.hongik.domain.user.UserRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//
//@RequiredArgsConstructor
//@Transactional
//@Service
//public class FirebaseService {
//
//    private final UserRepository userRepository;
//    private final StudySessionRepository studySessionRepository;
//    public static final String COLLECTION_NAME = "User";
//    private static final Firestore FIRE_STORE = FirestoreClient.getFirestore();
//
//    public List<Member> getAllMembers() throws ExecutionException, InterruptedException {
//        List<Member> list = new ArrayList<>();
//        ApiFuture<QuerySnapshot> future = FIRE_STORE.collection(COLLECTION_NAME).get();
//        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
//        for (QueryDocumentSnapshot document : documents) {
//            list.add(document.toObject(Member.class));
//        }
//
//        List<User> users = new ArrayList<>();
//        for (Member member : list) {
//            if (member.getId() == null || member.getNickname() == null || member.getDepartment() == null) {
//                continue;
//            }
//            User user = User.builder()
//                    .role(Role.USER)
//                    .department(member.getDepartment())
//                    .nickname(member.getNickname())
//                    .password(member.getId())
//                    .username(member.getEmail())
//                    .build();
//            users.add(user);
//        }
////        System.out.println("list.size() = " + list.size());
//
//        userRepository.saveAll(users);
//        return list;
//    }
//
//    public Member getMember(String id) throws ExecutionException, InterruptedException {
//        Query query = FIRE_STORE.collection(COLLECTION_NAME).whereEqualTo("id", id);
//        ApiFuture<QuerySnapshot> future = query.get();
//
//        try {
//            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
//            if (!documents.isEmpty()) {
//                return documents.get(0).toObject(Member.class);
//            }
//            return null;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * 11-04 오후 1:50 최신화
//     */
//    public List<StudyDay> getAllStudyDays() throws ExecutionException, InterruptedException {
//        List<User> users = userRepository.findAll();
//        List<StudySession> sessions = new ArrayList<>();
//        List<StudyDay> list = new ArrayList<>();
//        for (User user : users) {
//
//            if (user.getNickname().equals("기계")) {
//                // 1c7kWGDroJPxbUl8lixOg8W6AYy2
//                continue;
//            }
//
//            if (user.getNickname().equals("동차가보자")) {
//                // Gvt1UJUwkRQyiVdn4mOo38HXa9B3
//                continue;
//            }
//
//            if (user.getNickname().equals("before")) {
//                // vYQmuWNMVZVnzbFaAbkG4HAS1s72
//                continue;
//            }
//
//            if (user.getNickname().equals("깜까미")) {
//                // DGQhhvkSPvagN1ARmXPr3wEkgmF2
//                continue;
//            }
//
//            if (user.getNickname().equals("러이")) {
//                // KU2N5Tx2IYQ9QEpXJbZin5o7hqs1
//                continue;
//            }
//
//            if (user.getNickname().equals("람드")) {
//                // LdjPWimIR1PjR0gcRp4C1L0mTGr2
//                continue;
//            }
//
//            if (user.getNickname().equals("자몽소다")) {
//                // OkIBFLuGdHR2ytdi3cjEw26F4jc2
//                continue;
//            }
//
//            if (user.getNickname().equals("무구정광헌")) {
//                // QqcpsfFbQ9QfXzKI7MUPK0d1HnE2
//                continue;
//            }
//
//            if (user.getNickname().equals("점수를주호")) {
//                // VUJ31eGhHvYlxLXXf1vbTPbjg0J2
//                continue;
//            }
//
//
//            System.out.println("user.getNickname() = " + user.getNickname());
//            ApiFuture<QuerySnapshot> future =
//                    FIRE_STORE.collection(COLLECTION_NAME)
//                            .document(user.getPassword())
//                            .collection("StudyDay").get();
//
//            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
//            System.out.println("documents.size() = " + documents.size());
//
//            for (QueryDocumentSnapshot document : documents) {
//                if (document.getLong("duration").intValue() < 0) {
//                    continue;
//                }
//
//                Timestamp timestamp = document.getTimestamp("date");
//                LocalDateTime date = null;
//                if (timestamp != null) {
//                    date = LocalDateTime.ofInstant(
//                            Instant.ofEpochSecond(timestamp.getSeconds()), ZoneId.systemDefault()
//                    );
//
//                }
//
//                StudyDay studyDay = StudyDay.builder()
//                        .id(document.getId())
//                        .start(date.minusSeconds(document.getLong("duration").intValue()))
//                        .end(date)
//                        .duration(document.getLong("duration").intValue())
//                        .build();
//                StudySession studySession = StudySession.builder()
//                        .startTime(studyDay.getStart())
//                        .endTime(studyDay.getEnd())
//                        .user(user)
//                        .build();
//                sessions.add(studySession);
//                list.add(studyDay);
//            }
//        }
//        studySessionRepository.saveAll(sessions);
//
//        return list;
//    }
//}
