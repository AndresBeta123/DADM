package co.edu.unal.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
public class AndroidTicTacToeActivity extends Activity {
    // Buttons making up the board
    private Button mBoardButtons[];
    private Random playerRandom;
    // Various text displayed
    private TextView mInfoTextView,tiesText,hWinsText,aWinsText;
    // Represents the internal state of the game
    private TicTacToeGame mGame;
    private boolean  mGameOver;
    private int ties,hWins,aWins;
    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_RESET_ID = 1;
    static final int ABOUT_ID = 2;
    private BoardView mBoardView;
    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mHumanMediaPlayerError;
    MediaPlayer mComputerMediaPlayer;
    private char turno;
    private SharedPreferences mPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGame = new TicTacToeGame();
        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);

        ties = 0;
        hWins = 0;
        aWins = 0;
        playerRandom = new Random();


        tiesText = findViewById(R.id.ties);
        hWinsText = findViewById(R.id.human);
        aWinsText = findViewById(R.id.android);
        mInfoTextView = (TextView) findViewById(R.id.information);

        mPrefs = getSharedPreferences("ttt_prefs", MODE_PRIVATE);
        hWins = mPrefs.getInt("hWins", 0);
        aWins = mPrefs.getInt("aWins", 0);
        ties = mPrefs.getInt("ties", 0);


        if (savedInstanceState == null) {
            startNewGame();
        }
        else {
        // Restore the game's state
            mGame.setBoardState(savedInstanceState.getCharArray("board"));
            mGameOver = savedInstanceState.getBoolean("mGameOver");
            mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
            hWins = savedInstanceState.getInt("hWins");
            aWins = savedInstanceState.getInt("aWins");
            ties = savedInstanceState.getInt("ties");
            turno = savedInstanceState.getChar("turno");
        }

        displayScores();

    }

    // Set up the game board.
    private void startNewGame() {

        setTurno(TicTacToeGame.COMPUTER_PLAYER);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mGameOver = false;
                mGame.clearBoard();
                mBoardView.invalidate(); // Redraw the board
                playerRandom = new Random();
                if (playerRandom.nextInt(2) == 0) {
                    setTurno(TicTacToeGame.HUMAN_PLAYER);
                    mInfoTextView.setText(R.string.first_human);
                } else {
                    setTurno(TicTacToeGame.COMPUTER_PLAYER);
                    mInfoTextView.setText(R.string.turn_computer);
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    mBoardView.setGame(mGame);
                    setTurno(TicTacToeGame.HUMAN_PLAYER);
                    mInfoTextView.setText(R.string.turn_human);
                }
            }
        },1000);

    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            // Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;
            if (!mGameOver && mGame.getBoardOccupation(pos) == TicTacToeGame.OPEN_SPOT && getTurno() == TicTacToeGame.HUMAN_PLAYER ){
                setMove(TicTacToeGame.HUMAN_PLAYER, pos);
                mHumanMediaPlayer.start();
                mBoardView.setGame(mGame);
                setTurno(TicTacToeGame.COMPUTER_PLAYER);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        int winner = mGame.checkForWinner();
                        if (winner == 0) {
                            mInfoTextView.setText(R.string.turn_computer);
                            int move = mGame.getComputerMove();
                            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                            winner = mGame.checkForWinner();
                        }
                        if (winner == 0) {
                            mInfoTextView.setText(R.string.turn_human);
                            setTurno(TicTacToeGame.HUMAN_PLAYER);
                        }else if (winner == 1) {
                            mGameOver = true;
                            ties++;
                            mInfoTextView.setText(R.string.result_tie);
                            tiesText.setText(R.string.ties);
                            tiesText.append(String.valueOf(ties));
                        } else if (winner == 2) {
                            mGameOver = true;
                            hWins++;
                            mInfoTextView.setText(R.string.result_human_wins);
                            hWinsText.setText(R.string.human);
                            hWinsText.append(String.valueOf(hWins));
                        } else {
                            mGameOver = true;
                            aWins++;
                            mInfoTextView.setText(R.string.result_computer_wins);
                            aWinsText.setText(R.string.computer);
                            aWinsText.append(String.valueOf(aWins));
                        }
                    }
                }, 1000);
            } else if (!mGameOver && mGame.getBoardOccupation(pos) != TicTacToeGame.OPEN_SPOT) {
                mHumanMediaPlayerError.start();

            }
// So we aren't notified of continued events when finger is moved
            return false;
        }
        };

    private boolean setMove(char player, int location) {
        if(!mGameOver) {
            if (player == mGame.HUMAN_PLAYER) {
                if (mGame.setMove(player, location)) {
                    mHumanMediaPlayer.start();
                    mBoardView.invalidate(); // Redraw the board
                    return true;
                }
            } else if (player == mGame.COMPUTER_PLAYER) {
                if (mGame.setMove(player, location)) {
                    mComputerMediaPlayer.start();
                    mBoardView.invalidate(); // Redraw the board
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.new_game) {
            startNewGame();
            return true;
        } else if (itemId == R.id.ai_difficulty) {
            showDialog(DIALOG_DIFFICULTY_ID);
            return true;
        } else if (itemId == R.id.reset_score) {
            showDialog(DIALOG_RESET_ID);
            return true;
        }/*else if (itemId == R.id.about) {
            showDialog(ABOUT_ID);
            return true;
        }*/
        return false;
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id) {
            case DIALOG_DIFFICULTY_ID:
                builder.setTitle(R.string.difficulty_choose);
                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};
                // TODO: Set selected, an integer (0 to n-1), for the Difficulty dialog.
                int selected = 0;
                // selected is the radio button that should be selected.
                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss(); // Close dialog
                                // TODO: Set the diff level of mGame based on which item was selected.
                                if(item == 0){
                                    mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                }else if(item == 1){
                                    mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                                }else if(item == 2){
                                    mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
                                }
                                // Display the selected difficulty level
                                Toast.makeText(getApplicationContext(), levels[item],
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog = builder.create();
                break;
            case DIALOG_RESET_ID:
                // Reset
                hWins = 0;
                aWins = 0;
                ties = 0;

                SharedPreferences.Editor se = mPrefs.edit();
                se.putInt("hWins", hWins);
                se.putInt("aWins", aWins);
                se.putInt("ties", ties);
                se.commit();

                displayScores();
                break;
           /* case ABOUT_ID:
                builder = new AlertDialog.Builder(this);
                Context context = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.about_dialog, null);
                builder.setView(layout);
                builder.setPositiveButton("OK", null);
                dialog = builder.create();
                break;*/
        }
        return dialog;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sword);
        mHumanMediaPlayerError = MediaPlayer.create(getApplicationContext(), R.raw.shield);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.arrow);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mHumanMediaPlayer.release();
        mHumanMediaPlayerError.release();
        mComputerMediaPlayer.release();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharArray("board", mGame.getBoardState());
        outState.putBoolean("mGameOver", mGameOver);
        outState.putInt("hWins", Integer.valueOf(hWins));
        outState.putInt("aWins", Integer.valueOf(aWins));
        outState.putInt("ties", Integer.valueOf(ties));
        outState.putCharSequence("info", mInfoTextView.getText());
        outState.putChar("turno", turno);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGame.setBoardState(savedInstanceState.getCharArray("board"));
        mGameOver = savedInstanceState.getBoolean("mGameOver");
        mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
        hWins = savedInstanceState.getInt("hWins");
        aWins = savedInstanceState.getInt("aWins");
        ties = savedInstanceState.getInt("ties");
        turno = savedInstanceState.getChar("turno");
    }
    @Override
    protected void onStop() {
        super.onStop();
// Save the current scores
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("hWins", hWins);
        ed.putInt("aWins", aWins);
        ed.putInt("ties", ties);
        ed.commit();
    }

    private void displayScores() {
        hWinsText.setText(R.string.human);
        hWinsText.append(" ");
        hWinsText.append(Integer.toString(hWins));
        aWinsText.setText(R.string.computer);
        aWinsText.append(" ");
        aWinsText.append(Integer.toString(aWins));
        tiesText.setText(R.string.ties);
        tiesText.append(" ");
        tiesText.append(Integer.toString(ties));
    }
    public char getTurno() {
        return turno;
    }

    public void setTurno(char Rturno) {
        turno = Rturno;
    }


}