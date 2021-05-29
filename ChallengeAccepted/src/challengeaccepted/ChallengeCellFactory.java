package challengeaccepted;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ChallengeCellFactory implements Callback<ListView<Challenge>, ListCell<Challenge>> {

    @Override
    public ListCell<Challenge> call(ListView<Challenge> param) {
        return new CelulaChallenge();
    }
    
}
