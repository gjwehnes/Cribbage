

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.border.LineBorder;

import java.lang.Math;
import java.util.ArrayList;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class Cribbage extends JFrame {

	//TODO
	//-cannot flip cards while in container
	
	private CardTable cardTable;
	private CardTableEventAdapter cte;
	private CardGroup player1Hand;
	private CardGroup crib;
	private CardGroup player2Hand;
	private CardGroup player1CardPlay;	
	private CardGroup player2CardPlay;
	private ArrayList<Card> player1CardsPlayed = new ArrayList<Card>();
	private ArrayList<Card> player2CardsPlayed = new ArrayList<Card>();
	private JLabel lblPlayer1;
	private JLabel lblCrib;
	private JButton btnOk;
	private Deck deck ;
	private Card cutCard;
	private int player1Score = 0;
	private int player2Score = 0;
	private int cribScore = 0;
	private int currentPeggingScore = 0;
	private CardComparator comparator = new CardComparator();
	private JLabel lblPlayer1Score;
	private JLabel lblPlayer2Score;
	private JLabel lblCribScore;
	private JTextPane txtPrompt;
	private JLabel lblCurrentPeggingScore;
	
	//STATE VARIABLES
	private boolean isPlayer1Crib = true;
	private GameStates gameState = GameStates.INTRODUCTION;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Cribbage frame = new Cribbage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Cribbage() {
		setMaximumSize(new Dimension(800, 650));
		setTitle("Cribbage");
		setResizable(false);
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 900, 650);
		cardTable = new CardTable();
		setContentPane(cardTable);
		
		cte = new CardTableEventAdapter()
		{
			public void handleCardAddedToTableEvent(CardTable table, Card card) {handleCardAddedToTableLocal(table, card);}
			public void handleCardAddedToGroupEvent(CardGroup group, Card card) {handleCardAddedToGroupLocal(group, card);}
			public void handleCardRemovedFromGroupEvent(CardGroup group, Card card) {handleCardRemovedFromGroupLocal(group, card);}
			public void handleCardFlippedEvent(Container source, Card card) {handleCardFlippedLocal(source, card);}
			public DragStates handleCardDragging(Card card, Container dropTarget) {return handleCardDraggingLocal(card, dropTarget);}
			public void handleCardDragged(Card card) {handleCardDraggedLocal(card);}
		};
		
		cardTable.addCardTableEventListener(cte);
		
		
		player1Hand = new CardGroup(96,0);
		player1Hand.setName("player1Hand");
		player1Hand.setvSpace(0);
		player1Hand.sethBorder(4);
		player1Hand.setvBorder(4);
		player1Hand.sethSpace(76);
		player1Hand.setBorder(new LineBorder(Color.RED, 2));
		player1Hand.setForeground(Color.GREEN);
		player1Hand.setBackground(Color.GREEN);
		player1Hand.setBounds(10, 37, 314, 110);
		cardTable.add(player1Hand);
		player1Hand.setLayout(null);
		
		lblPlayer1 = new JLabel("Player 1");
		lblPlayer1.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPlayer1.setBounds(10, 8, 314, 30);
		cardTable.add(lblPlayer1);
		
		lblPlayer1Score = new JLabel("29");
		lblPlayer1Score.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer1Score.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPlayer1Score.setBounds(288, 152, 36, 30);
		cardTable.add(lblPlayer1Score);
		
		player2Hand = new CardGroup(96, 0);
		player2Hand.setLayout(null);
		player2Hand.setvSpace(0);
		player2Hand.setvBorder(4);
		player2Hand.setName("handGroup");
		player2Hand.sethSpace(76);
		player2Hand.sethBorder(4);
		player2Hand.setForeground(Color.GREEN);
		player2Hand.setBorder(new LineBorder(Color.RED, 2));
		player2Hand.setBackground(Color.GREEN);
		player2Hand.setBounds(10, 471, 314, 110);
		cardTable.add(player2Hand);
		
		JLabel lblPlayer2 = new JLabel("Player 2");
		lblPlayer2.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPlayer2.setBounds(10, 578, 314, 30);
		cardTable.add(lblPlayer2);
		
		lblPlayer2Score = new JLabel("29");
		lblPlayer2Score.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayer2Score.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPlayer2Score.setBounds(288, 431, 36, 30);
		cardTable.add(lblPlayer2Score);

		crib = new CardGroup(96,0);
		crib.setName("cribGroup");
		crib.setvSpace(0);
		crib.sethBorder(4);
		crib.setvBorder(4);
		crib.sethSpace(76);
		crib.setBorder(new LineBorder(Color.BLUE, 2));
		crib.setForeground(Color.GREEN);
		crib.setBackground(Color.GREEN);
		crib.setBounds(352, 37, 314, 110);
		cardTable.add(crib);
		crib.setLayout(null);
		
		lblCrib = new JLabel("Crib");
		lblCrib.setHorizontalAlignment(SwingConstants.CENTER);
		lblCrib.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblCrib.setBounds(352, 11, 314, 30);
		cardTable.add(lblCrib);
		
		lblCribScore = new JLabel("29");
		lblCribScore.setHorizontalAlignment(SwingConstants.CENTER);
		lblCribScore.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblCribScore.setBounds(630, 152, 36, 30);
		cardTable.add(lblCribScore);
		
		deck = new Deck();
		deck.setBorder(new LineBorder(Color.MAGENTA, 2));
		deck.setLocation(566, 276);
		cardTable.add(deck);
		
		btnOk = new JButton("Ok");
		btnOk.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnOk_mouseClicked(arg0);
			}
		});
		btnOk.setEnabled(false);
		btnOk.setBounds(395, 397, 82, 23);
		cardTable.add(btnOk);
		
		JLabel lblCribBoard = new JLabel();
		lblCribBoard.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		lblCribBoard.setBounds(676, 8 , 208, 600);
		lblCribBoard.setIcon(new ImageIcon("res\\cribbage-board.png"));

		cardTable.add(lblCribBoard);
		
		player1CardPlay = new CardGroup();
		player1CardPlay.setBounds(10, 160, 80, 110);
		cardTable.add(player1CardPlay);
		player1CardPlay.setName("player1PeggingPile");
		player1CardPlay.setvSpace(0);
		player1CardPlay.sethBorder(4);
		player1CardPlay.setvBorder(4);
		player1CardPlay.sethSpace(76);
		player1CardPlay.setBorder(new LineBorder(Color.RED, 2));
		player1CardPlay.setForeground(Color.GREEN);
		player1CardPlay.setBackground(Color.GREEN);
		player1CardPlay.setLayout(null);
		
		player2CardPlay = new CardGroup();
		player2CardPlay.setLayout(null);
		player2CardPlay.setvSpace(0);
		player2CardPlay.setvBorder(4);
		player2CardPlay.setName("player2PeggingPile");
		player2CardPlay.sethSpace(76);
		player2CardPlay.sethBorder(4);
		player2CardPlay.setForeground(Color.GREEN);
		player2CardPlay.setBorder(new LineBorder(Color.RED, 2));
		player2CardPlay.setBackground(Color.GREEN);
		player2CardPlay.setBounds(10, 350, 80, 110);
		cardTable.add(player2CardPlay);
		
		lblCurrentPeggingScore = new JLabel("");
		lblCurrentPeggingScore.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrentPeggingScore.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblCurrentPeggingScore.setBounds(10, 295, 174, 30);
		cardTable.add(lblCurrentPeggingScore);
		
		txtPrompt = new JTextPane();
		txtPrompt.setFont(new Font("Tahoma", Font.BOLD, 16));
		txtPrompt.setBackground(Color.GREEN);
		txtPrompt.setEditable(false);
		txtPrompt.setBounds(338, 275, 218, 107);
		txtPrompt.setAlignmentX(CENTER_ALIGNMENT);
		txtPrompt.setAlignmentY(CENTER_ALIGNMENT);
		cardTable.add(txtPrompt);
						
		setControls();
		
	}

	private void handleCardAddedToTableLocal(CardTable table, Card card){
		System.out.println("Cribbage.handleCardAddedToTableLocal: " + card.getName() + " was added to " + table.getName());
		card.setCanFlip(true);
		checkStateSwitch(UserActions.CARD_DRAGGED);
		setControls();
	}
	
	private void handleCardAddedToGroupLocal(CardGroup group, Card card){		
		System.out.println("Cribbage.handleCardAddedToGroupLocal: " + card.getName() + " was added to " + group.getName());
		card.setCanFlip(false);
		checkStateSwitch(UserActions.CARD_DRAGGED);
		setControls();		
	}
	
	private void handleCardRemovedFromGroupLocal(CardGroup group, Card card){
		System.out.println("Cribbage.handleCardRemovedFromGroupLocal: " + card.getName() + " was removed from " + group.getName());
		card.setCanFlip(true);		
		checkStateSwitch(UserActions.CARD_DRAGGED);
		setControls();		
	}
	
	private void handleCardFlippedLocal(Container source, Card card){
		System.out.println("Cribbage.handleCardFlippedLocal: " + card.getName() + " in " + source.getName() + " was flipped");
		checkStateSwitch(UserActions.CARD_DRAGGED);
		setControls();
	}
	
	private DragStates handleCardDraggingLocal(Card card, Container dropTarget){
		
		/*				
		DragStates dragPermission = DragStates.ALLOW;
		if (allowDrag) {
			System.out.println("Cribbage.handleCardDraggingLocal: " + card.getName());
			ArrayList<Card> related = new ArrayList<Card>();
			//related.add(related1);
			card.setRelated(related);
		}
		
		
		
		setControls();
		
		*/
		return DragStates.ALLOW;
		
	}
	
	private void handleCardDraggedLocal(Card card){
		System.out.print("Cribbage.handleCardDraggedLocal: " + card.getName());
		System.out.println("; location = (" + card.getX() + "," + card.getY() + ")");
		card.setRelated(null);

		card.validate();
		card.repaint();
				
		checkStateSwitch(UserActions.CARD_DRAGGED);
		setControls();

	}
	
	private void dealCards(){
		
		cardTable.clearTable();
		
		deck.reset();
		for (int i =1 ; i <= 6; i++){
			Card player1Card =deck.drawCard();
			cardTable.add(player1Card);
			player1Card.setLocation(deck.getX(), deck.getY() );
			player1Card.setLocation(10 + (i - 1) * 76 , 159 );
			player1Card.setFaceUp(true);
			player1Card.setCanDrag(true);
			player1Card.setCanFlip(false);
			
			Card player2Card =deck.drawCard();
			cardTable.add(player2Card);
			player2Card.setCanDrag(false);
			player2Card.setFaceUp(false);
			player2Card.setCanFlip(false);
			if (i <= 4) {
				player2Hand.add(player2Card);				
			}
			else {
				crib.add(player2Card);
			}			
		}
		
		crib.reOrder();
		crib.repaint();
		
		player2Hand.reOrder();
		player2Hand.repaint();
						
		setControls();
		
	}

	private int calculateScore(ArrayList<Card> hand, Suit cutSuit) {

		int points = 0;
		
		if (hand.size() == 5) {
		
			for (int counter = 1; counter <= 31; counter++) {
				ArrayList<Card> subset = new ArrayList<Card>();
				String binary = Integer.toBinaryString(counter);
				binary = String.format("%05d", Integer.decode(binary));
				for (int place = 0; place <= 4; place++) {
					if (binary.substring(place, place + 1).equals("1")) {
						subset.add(hand.get(place));
					}
				}
				points += calculatePartialScore(subset, cutSuit);				
			}
		}

		return points;
	}

	private int calculatePartialScore(ArrayList<Card> hand, Suit cutSuit) {
		
		int points = 0;

		hand.sort(new CardComparator());			

		//base case - zero cards
		if (hand.size() == 0) {
			return 0;
		}
		else if (hand.size() == 1) {
			Card firstCard = hand.get(0);
			if ((firstCard.getFaceValue() == FaceValue.JACK) && (firstCard.getSuit() == cutSuit)) {
				points += 1;
			}
		}
		else if (hand.size() == 2) {
			Card firstCard = hand.get(0);
			Card secondCard = hand.get(1);
			if (firstCard.getFaceValue() == secondCard.getFaceValue()) {
				points += 2;
			}			
		}
		else {
			
			boolean isRun = true;
			for (int i = 1; i < hand.size(); i++) {
				if (hand.get(i - 1).getFaceValue().ordinal() != hand.get(i).getFaceValue().ordinal() - 1) {
					isRun = false;
					break;
				}			
			}
			if (isRun) {
				if (hand.size() == 3) {
					points += 3;
				}
				else if (hand.size() == 4) {
					points -= 2;
				}
			}
		}

		int sum = 0;
		for (int i = 0; i < hand.size(); i++) {
			sum += getPointValue(hand.get(i).getFaceValue());
		}
		if (sum == 15) {
			points += 2;
		}
									
		System.out.printf("calculateScore for %s: %d\n", hand.toString(), points);
		return points;
	}
	
	private int getPointValue(FaceValue faceValue) {
		//this method properly belongs in this class as the game determines
		//the points value of cards
		switch (faceValue) {
		case ACE:
			return 1;
		case TWO:
			return 2;
		case THREE:
			return 3;
		case FOUR:
			return 4;
		case FIVE:
			return 5;
		case SIX:
			return 6;
		case SEVEN:
			return 7;
		case EIGHT:
			return 8;
		case NINE:
			return 9;
		case TEN:
        case KING:
        case QUEEN:
        case JACK:
        	return 10;
        default:
        	return 0;			
		}		
	}
	
	private void addPeggingScore(Card newCard, boolean player1) {
		currentPeggingScore = currentPeggingScore + getPointValue(newCard.getFaceValue());
		if ((currentPeggingScore == 15) || (currentPeggingScore == 31)) {
			if (player1) {
				this.txtPrompt.setText(this.txtPrompt.getText() + "\n+2 for Player 1!");
			}
			else {
				this.txtPrompt.setText(this.txtPrompt.getText() + "\n+2 for Player 2!");				
			}
		}
		if (currentPeggingScore >= 31) {
			this.currentPeggingScore = 0;			
		}
	}
	
	private void checkStateSwitch(UserActions userAction) {
		
		//User action occurred - should the state of the game switch?
		//if so, perform all necessary actions
		if (gameState == GameStates.INTRODUCTION){
			if (userAction == UserActions.OK_CLICKED); {
				dealCards();
				this.gameState = GameStates.CARDS_DEALT;
				return;
			}
		}
		
		if (gameState == GameStates.CARDS_DEALT){
			if (userAction == UserActions.OK_CLICKED) {
				cutCard = this.deck.drawCard();
				cutCard.setLocation(this.deck.getX() - 16, this.deck.getY() - 16);
				cutCard.setCanFlip(true);
				cutCard.setFaceUp(true);
				cutCard.setCanDrag(true);
				this.cardTable.add(cutCard);
				
				ArrayList<Card> player1Cards = this.player1Hand.getCards();
				player1Cards.add(cutCard);
				player1Score = calculateScore(player1Cards, cutCard.getSuit());
				
				ArrayList<Card> player2Cards = this.player2Hand.getCards();
				player2Cards.add(cutCard);
				player2Score = calculateScore(player2Cards, cutCard.getSuit());

				ArrayList<Card> cribCards = this.crib.getCards();
				cribCards.add(cutCard);
				cribScore = calculateScore(cribCards, cutCard.getSuit());
				
				for (Card cribCard : cribCards) {
					cribCard.setFaceUp(false);
					cribCard.setCanDrag(false);
				}

				this.gameState = GameStates.PLAYER1_PRE_PEG;
				return;
			}
		}
		
		if (gameState == GameStates.PLAYER1_PRE_PEG){
			boolean cardPlayed = (this.player1CardPlay.getComponentCount() == 1);
			if (cardPlayed) {
				Card player1Card = this.player1CardPlay.getCards().get(0);
				//TODO: rules for resetting / scoring
				
				addPeggingScore(player1Card, true);
				//continue on into post_peg!
				gameState = GameStates.PLAYER1_POST_PEG;
			}
		}

		if (gameState == GameStates.PLAYER1_POST_PEG){
			if (this.player1Hand.getComponentCount() == 0 && this.player2Hand.getComponentCount() == 0) {
				//neither player has cards remaining, so transition to COUNT
				gameState = GameStates.COUNT;
			}
			else if (this.player2Hand.getComponentCount() > 0) {
				//player 2 has cards remaining, so transition to PLAYER2_PRE_PEG
				gameState = GameStates.PLAYER2_PRE_PEG;
			}
			else {
				//player 2 has no cards, so revert back to player 1
				gameState = GameStates.PLAYER1_PRE_PEG;				
			}
		}
		if (gameState == GameStates.PLAYER2_PRE_PEG){
			if (this.player2Hand.getComponentCount() >= 1) {
				//TODO: AI for picking an appropriate card
				Card player2Card = this.player2Hand.getCards().get(0);
				this.player2Hand.remove(player2Card);
				this.player2CardPlay.add(player2Card);
				this.player2CardPlay.reOrder();
				player2Card.setFaceUp(true);
				//TODO: rules for resetting / scoring
				addPeggingScore(player2Card, false);
				gameState = GameStates.PLAYER2_POST_PEG;
			}
			else {
				//this case should never be reached, but to be safe, switch to POST_PEG
				gameState = GameStates.PLAYER2_POST_PEG;
			}
		}
		if (gameState == GameStates.PLAYER2_POST_PEG){
			if (userAction == UserActions.OK_CLICKED ) {
				if (this.player1CardPlay.getComponentCount() == 1) {
					Card player1Card = this.player1CardPlay.getCards().get(0);
					this.player1CardPlay.remove(player1Card);
					this.cardTable.add(player1Card);
					player1Card.setLocation(this.player1CardPlay.getX() + 144 + (this.player1Hand.getComponentCount() * 16), this.player1CardPlay.getY());
					player1Card.setCanDrag(false);
					player1CardsPlayed.add(player1Card);
					System.out.printf("player1CardPlay.getComponentCount(): %d", player1CardPlay.getComponentCount());
				}
				if (this.player2CardPlay.getComponentCount() == 1) {
					Card player2Card = this.player2CardPlay.getCards().get(0);
					this.player2CardPlay.remove(player2Card);
					this.cardTable.add(player2Card);					
					player2Card.setLocation(this.player2CardPlay.getX() + 144 + (this.player2Hand.getComponentCount() * 16), this.player2CardPlay.getY());
					player2Card.setCanDrag(false);
					player2CardsPlayed.add(player2Card);					
					System.out.printf("player2CardPlay.getComponentCount(): %d", player2CardPlay.getComponentCount());
				}
				if (this.player1Hand.getComponentCount() == 0 && this.player2Hand.getComponentCount() == 0) {
					//neither player has cards remaining, so transition to COUNT
					gameState = GameStates.COUNT;
				}
				else if (this.player1Hand.getComponentCount() > 0) {
					//player 1 has cards remaining, so transition to PLAYER1_PRE_PEG
					gameState = GameStates.PLAYER1_PRE_PEG;
				}
				else {
					//player 1 has no cards, so revert back to player 2
					gameState = GameStates.PLAYER2_PRE_PEG;				
				}
			}
		}
		if (gameState == GameStates.COUNT){
			
		}

	}
	
	private void setControls(){

		switch (gameState) {
/*		case INTRODUCTION:
			this.txtPrompt.setText("Welcome to Crib");
			break;
		case CARDS_DEALT:
			this.txtPrompt.setText("Select your cards");
			break;
		case PLAYER1_PRE_PEG:
			this.txtPrompt.setText("Pegging round\nPlay a card!");
			break;
		case PLAYER2_PRE_PEG:
			this.txtPrompt.setText("Pegging round");
			break;
		case COUNT:
			this.txtPrompt.setText("Count!");
			break;
*/
		default:
			this.txtPrompt.setText(gameState.toString());
		}
		
		int cardsInHand = this.player1Hand.getComponentCount();
		int cardsInCrib = this.crib.getComponentCount();
		boolean cardsSelected = ((cardsInHand == 4) && (cardsInCrib == 4));
		System.out.printf("Cribbage.setControls: Status=%s; cardInHand=%d; cardsInCrib=%d;", gameState.toString(), cardsInHand, cardsInCrib);
				
		this.deck.setEnabled(false);
		
		boolean peggingRound = gameState == GameStates.PLAYER1_PRE_PEG || 
				gameState == GameStates.PLAYER1_POST_PEG  ||
				gameState == GameStates.PLAYER2_PRE_PEG  ||
				gameState == GameStates.PLAYER2_POST_PEG;

		this.lblPlayer1Score.setText(gameState == GameStates.COUNT ? Integer.toString(player1Score) :"");
		this.lblPlayer2Score.setText(gameState == GameStates.COUNT ? Integer.toString(player2Score) :"");
		this.lblCribScore.setText(gameState == GameStates.COUNT ? Integer.toString(cribScore) :"");
		this.lblCurrentPeggingScore.setText(peggingRound ? "Count: " + Integer.toString(currentPeggingScore) : "");
		this.player1CardPlay.setVisible(peggingRound);
		this.player2CardPlay.setVisible(peggingRound);

		//dragging and dropping		
		this.player1Hand.setCanDrop(gameState == GameStates.CARDS_DEALT && (player1Hand.getComponentCount() < 4));
		this.crib.setCanDrop(gameState == GameStates.CARDS_DEALT && (crib.getComponentCount() < 4));
		this.player2Hand.setCanDrop(false);
		this.player1CardPlay.setCanDrop(gameState == GameStates.PLAYER1_PRE_PEG && (player1CardPlay.getComponentCount() < 1));
		this.player2CardPlay.setCanDrop(false);
		this.cardTable.setCanDrop(gameState == GameStates.CARDS_DEALT);

		//ordering
		player1Hand.sort(comparator);

		//buttons
		this.btnOk.setEnabled(gameState == GameStates.INTRODUCTION
				|| ((gameState == GameStates.CARDS_DEALT) && cardsSelected)
				|| ((gameState == GameStates.PLAYER2_POST_PEG)));
		
		if (cutCard != null) cutCard.setFaceUp(peggingRound || (gameState == GameStates.COUNT ));
						
		this.validate();
		this.repaint();
	}
	private void btnOk_mouseClicked(MouseEvent arg0) {
		if (this.btnOk.isEnabled() == true) {
			checkStateSwitch(UserActions.OK_CLICKED);		
			setControls();
		}
	}
}

