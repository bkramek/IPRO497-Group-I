import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DATABASECONNECTIONS {

    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Add this line to any file where you access the database

    // RATINGS

    public void submitRating(int reservationId, int rating) {
        /**
         * Submit a rating for the previous occupant of a room (1-5)
         * @param reservationId
         * @param rating
         * @return N/A
         */
        DocumentReference reservationDocRef = db.collection("reservations").document(Integer.toString(reservationId));
        reservationDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                int lastUserId;
                if (task.isSuccessful()) {
                    DocumentSnapshot reservationDoc = task.getResult();

                    if (reservationDoc.getBoolean("rating_submitted")) {
                        return;
                    }

                    db.collection("reservations")
                            .whereEqualTo("institution_id", reservationDoc.getData().get("institution_id"))
                            .whereEqualTo("room_id", reservationDoc.getData().get("room_id"))
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                long prevUserId = 0;
                                long mostRecentTime = -1;
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    if (doc.getLong("check_out_time") > mostRecentTime) {
                                        prevUserId = doc.getLong("user_id");
                                        mostRecentTime = doc.getLong("check_out_time");
                                    }
                                }
                                if (prevUserId > 0 && mostRecentTime > -1) {
                                    Map<String, Object> ratingData = new HashMap<>();
                                    ratingData.put("rater_user_id", reservationDoc.get("user_id"));
                                    ratingData.put("rated_user_id", prevUserId);
                                    ratingData.put("rating", rating);
                                    ratingData.put("timestamp", (int) System.currentTimeMillis() / 1000);
                                    db.collection("reservations").document(reservationDoc.getId()).update("rating_submitted", true);
                                    db.collection("ratings").add(ratingData);
                                } else {
                                    // Error
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    public Map<String, Object> getUserRatings(int userId) {
        /**
         * Get a user's rating data
         * @param userId
         * @return HashMap<String, Object>
         */
        Map<String, Object> ratingData = new HashMap<>();
        db.collection("ratings").whereEqualTo("rated_user_id", userId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    double avgRating;
                    ArrayList<Long> ratings = new ArrayList<Long>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        ratings.add(doc.getLong("rating"));
                    }
                    long ratingSum = 0;
                    for (long x : ratings) {
                        ratingSum += x;
                    }
                    avgRating = (double) ratingSum / ratings.size();
                    ratingData.put("avgRating", avgRating);
                    ratingData.put("allRatings", ratings);
                }
            }
        });
        try {
            ratingData.get("avgRating");
            ratingData.get("allRatings");
            return ratingData;
        } catch (Exception e) {
            // Error
            return null;
        }
    }

    // ROOMS

    public ArrayList<Map> getListOfRooms(int institutionId) {
        /**
         * Returns an arraylist of maps for each room with it's building name, room number, and database ID
         * @param institutionId
         * @return ArrayList<Map>
         */
        ArrayList<Map> rooms = new ArrayList<>();
        db.collection("rooms").whereEqualTo("institution_id", institutionId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Map<String, Object> roomData = new HashMap<>();
                        roomData.put("buildingName", doc.getString("building_name"));
                        roomData.put("roomNumber", doc.getString("room_number"));
                        roomData.put("roomId", doc.getId());
                        rooms.add(roomData);
                    }
                }
            }
        });
        return rooms;
    }

    // USERS

    public ArrayList<Map> getListOfUsers(int institutionId) {
        /**
         * Returns a list of users for a specific institution
         * @param institutionId
         * @return ArrayList<Map>
         */
        ArrayList<Map> users = new ArrayList<>();
        db.collection("users").whereEqualTo("institution_id", institutionId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("emailAddress", doc.getString("email_address"));
                                userData.put("firstName", doc.getString("first_name"));
                                userData.put("lastName", doc.getString("last_name"));
                                userData.put("userId", doc.getId());
                                users.add(userData);
                            }
                        }
                    }
                });
        return users;
    }
}