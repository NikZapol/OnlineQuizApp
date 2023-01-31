import android.support.test.runner.AndroidJUnit4;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class FirebaseLoginTest {

    @Mock
    private FirebaseAuth firebaseAuth;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoginWithFirebase() {
        when(firebaseAuth.signInWithEmailAndPassword("test@example.com", "password"))
                .thenReturn(new AuthResult());
        AuthResult authResult = firebaseAuth.signInWithEmailAndPassword("test@example.com", "password").getResult();
        assertNotNull(authResult);
    }
}
