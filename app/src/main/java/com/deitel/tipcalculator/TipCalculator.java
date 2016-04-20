// TipCalculator.java
// Calculates bills using 5, 10, 15 and custom percentage tips.
package com.deitel.tipcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

// main Activity class for the TipCalculator
public class TipCalculator extends Activity 
{
   // constants used when saving/restoring state
   private static final String BILL_TOTAL = "BILL_TOTAL";
   private static final String CUSTOM_PERCENT = "CUSTOM_PERCENT";
   private static final String TAX = "TAX";
    private static final String PARTY = "party";
   
   private double currentBillTotal; // bill amount entered by the user
   private double tax; // tax amount entered by user
   private int party; // party number entered by user
   private int currentCustomPercent; // tip % set with the SeekBar
   private EditText tip10EditText; // displays 10% tip
   private EditText total10EditText; // displays total with 10% tip
   private EditText tip15EditText; // displays 15% tip
   private EditText total15EditText; // displays total with 15% tip
   private EditText billEditText; // accepts user input for bill total
   private EditText tip20EditText; // displays 20% tip
   private EditText total20EditText;  // displays total with 20% tip
   private TextView customTipTextView; // displays custom tip percentage
   private EditText tipCustomEditText; // displays custom tip amount
   private EditText totalCustomEditText; // displays total with custom tip
   private EditText tipCustomTEditText; // displays custom tip amount without tax
   private EditText totalCustomTEditText; // displays total with custom taxless tip
   private EditText taxEditText; // accepts user input for tax
   private EditText partyEditText; // accepts user input for number of party members
   private TextView splitNoTax;
   private TextView splitTax;

   // Called when the activity is first created.
   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
      super.onCreate(savedInstanceState); // call superclass's version
      setContentView(R.layout.main); // inflate the GUI

      // check if app just started or is being restored from memory
      if ( savedInstanceState == null ) // the app just started running
      {
         currentBillTotal = 0.0; // initialize the bill amount to zero
         tax = 0.0; // initialize the tax amount to zero
         party = 1; // initialize the party number to zero
         currentCustomPercent = 18; // initialize the custom tip to 18%
      } // end if
      else // app is being restored from memory, not executed from scratch
      {
         // initialize the bill amount to saved amount
         currentBillTotal = savedInstanceState.getDouble(BILL_TOTAL); 
         
         // initialize the custom tip to saved tip percent 
         currentCustomPercent = 
            savedInstanceState.getInt(CUSTOM_PERCENT);

          // initialize the tax amount to saved amount
         tax = savedInstanceState.getDouble(TAX);

         // initialize the party number to saved amount
         party = savedInstanceState.getInt(PARTY);
      } // end else
      
      // get references to the 10%, 15% and 20% tip and total EditTexts
      tip10EditText = (EditText) findViewById(R.id.tip10EditText);
      total10EditText = (EditText) findViewById(R.id.total10EditText);
      tip15EditText = (EditText) findViewById(R.id.tip15EditText);
      total15EditText = (EditText) findViewById(R.id.total15EditText);
      tip20EditText = (EditText) findViewById(R.id.tip20EditText);
      total20EditText = (EditText) findViewById(R.id.total20EditText);
      
      // get the TextView displaying the custom tip percentage
      customTipTextView = (TextView) findViewById(R.id.customTipTextView);
      
      // get the custom tip and total EditTexts 
      tipCustomEditText = (EditText) findViewById(R.id.tipCustomEditText);
      totalCustomEditText = 
         (EditText) findViewById(R.id.totalCustomEditText);

       // get the custom tax tip and total EditTexts
       tipCustomTEditText = (EditText) findViewById(R.id.tipCustomTEditText);
       totalCustomTEditText =
               (EditText) findViewById(R.id.totalCustomTEditText);

       // get the custom party split texts
       splitNoTax = (EditText) findViewById(R.id.splitNoTax);
       splitTax =
               (EditText) findViewById(R.id.splitTax);

      // get the billEditText 
      billEditText = (EditText) findViewById(R.id.billEditText);

      // billEditTextWatcher handles billEditText's onTextChanged event
      billEditText.addTextChangedListener(billEditTextWatcher);

      // get the taxEditText
       taxEditText = (EditText) findViewById(R.id.taxEditText);

       // taxEditTextWatcher handles taxEditText's onTextChanged event
       taxEditText.addTextChangedListener(taxEditTextWatcher);

       // get the partyEditText
       partyEditText = (EditText) findViewById(R.id.partyEditText);

       // partyEditTextWatcher handles partyEditText's onTextChanged event
       partyEditText.addTextChangedListener(partyTextWatcher);

      
      // get the SeekBar used to set the custom tip amount
      SeekBar customSeekBar = (SeekBar) findViewById(R.id.customSeekBar);
      customSeekBar.setOnSeekBarChangeListener(customSeekBarListener);
   } // end method onCreate

   // updates 10, 15 and 20 percent tip EditTexts
   private void updateStandard() 
   {
      // calculate bill total with a ten percent tip
      double tenPercentTip = currentBillTotal * .1;
      double tenPercentTotal = currentBillTotal + tenPercentTip;

      // set tipTenEditText's text to tenPercentTip
      tip10EditText.setText(String.format("%.02f", tenPercentTip));

      // set totalTenEditText's text to tenPercentTotal
      total10EditText.setText(String.format("%.02f", tenPercentTotal));

      // calculate bill total with a fifteen percent tip
      double fifteenPercentTip = currentBillTotal * .15;
      double fifteenPercentTotal = currentBillTotal + fifteenPercentTip;

      // set tipFifteenEditText's text to fifteenPercentTip
      tip15EditText.setText(String.format("%.02f", fifteenPercentTip));

      // set totalFifteenEditText's text to fifteenPercentTotal
      total15EditText.setText(
         String.format("%.02f", fifteenPercentTotal));

      // calculate bill total with a twenty percent tip
      double twentyPercentTip = currentBillTotal * .20;
      double twentyPercentTotal = currentBillTotal + twentyPercentTip;

      // set tipTwentyEditText's text to twentyPercentTip
      tip20EditText.setText(String.format("%.02f", twentyPercentTip));

      // set totalTwentyEditText's text to twentyPercentTotal
      total20EditText.setText(String.format("%.02f", twentyPercentTotal));
   } // end method updateStandard

   // updates the custom tip, total EditTexts party split and splitEditTexts
   private void updateCustom() 
   {
      // set customTipTextView's text to match the position of the SeekBar
      customTipTextView.setText(currentCustomPercent + "%");

       // calculate the custom tip amount without tax
       double customTTipAmount =
               (currentBillTotal / ((tax * .01)+1)) * currentCustomPercent*.01;
       // calculate the total bill, including tip w/o tax
       double customTTotalAmount = currentBillTotal + customTTipAmount;


      // calculate the custom tip amount
      double customTipAmount = 
         currentBillTotal * currentCustomPercent * .01;

      // calculate the total bill, including the custom tip
      double customTotalAmount = currentBillTotal + customTipAmount;

       // calculate the split tip w/o tax
       double withoutTaxTip = customTTipAmount / party;

       // calculate the split tip w/tax
       double withTaxTip = customTipAmount / party;


       // display the tip and total bill amount.
       splitNoTax.setText(String.format("%.02f", withoutTaxTip));
       splitTax.setText(
               String.format("%.02f", withTaxTip));

      // display the tip and total bill amounts
      tipCustomEditText.setText(String.format("%.02f", customTipAmount));
       totalCustomEditText.setText(
               String.format("%.02f", customTotalAmount));

       // display the taxless tip and total bill amounts
       tipCustomTEditText.setText(String.format("%.02f", customTTipAmount));
       totalCustomTEditText.setText(
               String.format("%.02f", customTTotalAmount));
   } // end method updateCustom

   // save values of billEditText and customSeekBar
   @Override
   protected void onSaveInstanceState(Bundle outState)
   {
      super.onSaveInstanceState(outState);
      
      outState.putDouble(BILL_TOTAL, currentBillTotal);
      outState.putInt(CUSTOM_PERCENT, currentCustomPercent);
   } // end method onSaveInstanceState
   
   // called when the user changes the position of SeekBar
   private OnSeekBarChangeListener customSeekBarListener = 
      new OnSeekBarChangeListener() 
   {
      // update currentCustomPercent, then call updateCustom
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress,
         boolean fromUser) 
      {
         // sets currentCustomPercent to position of the SeekBar's thumb
         currentCustomPercent = seekBar.getProgress();
         updateCustom(); // update EditTexts for custom tip and total
      } // end method onProgressChanged

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) 
      {
      } // end method onStartTrackingTouch

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) 
      {
      } // end method onStopTrackingTouch
   }; // end OnSeekBarChangeListener

   // event-handling object that responds to billEditText's events
   private TextWatcher billEditTextWatcher = new TextWatcher() 
   {
      // called when the user enters a number
      @Override
      public void onTextChanged(CharSequence s, int start, 
         int before, int count) 
      {         
         // convert billEditText's text to a double
         try
         {
            currentBillTotal = Double.parseDouble(s.toString());
         } // end try
         catch (NumberFormatException e)
         {
            currentBillTotal = 0.0; // default if an exception occurs
         } // end catch 

         // update the standard and custom tip EditTexts
         updateStandard(); // update the 10, 15 and 20% EditTexts
         updateCustom(); // update the custom tip EditTexts
      } // end method onTextChanged


      @Override
      public void afterTextChanged(Editable s) 
      {
      } // end method afterTextChanged

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count,
         int after) 
      {
      } // end method beforeTextChanged
   }; // end billEditTextWatcher
    private TextWatcher taxEditTextWatcher = new TextWatcher()
    {
        // called when the user enters a number
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count)
        {
            // convert taxEditText's text to a double
            try
            {
                tax = Double.parseDouble(s.toString());
            } // end try
            catch (NumberFormatException e)
            {
                tax = 0.0; // default if an exception occurs
            } // end catch

            updateCustom(); // update the custom tip EditTexts
        } // end method onTextChanged


        @Override
        public void afterTextChanged(Editable s)
        {
        } // end method afterTextChanged

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after)
        {
        } // end method beforeTextChanged
    }; // end taxEditTextWatcher

    // called when the user enters a party number
    private TextWatcher partyTextWatcher = new TextWatcher()
    {
        // called when the user enters a number
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count)
        {
            // convert partyEditText's text to an int
            try
            {
                party = Integer.parseInt(s.toString());
            } // end try
            catch (NumberFormatException e)
            {
                party = 1; // default if an exception occurs
            } // end catch

            updateCustom(); // update the custom tip EditTexts
        } // end method onTextChanged


        @Override
        public void afterTextChanged(Editable s)
        {
        } // end method afterTextChanged

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after)
        {
        } // end method beforeTextChanged
    }; // end partyTextWatcher
} // end class TipCalculator
   
/*************************************************************************
* (C) Copyright 1992-2012 by Deitel & Associates, Inc. and               *
* Pearson Education, Inc. All Rights Reserved.                           *
*                                                                        *
* DISCLAIMER: The authors and publisher of this book have used their     *
* best efforts in preparing the book. These efforts include the          *
* development, research, and testing of the theories and programs        *
* to determine their effectiveness. The authors and publisher make       *
* no warranty of any kind, expressed or implied, with regard to these    *
* programs or to the documentation contained in these books. The authors *
* and publisher shall not be liable in any event for incidental or       *
* consequential damages in connection with, or arising out of, the       *
* furnishing, performance, or use of these programs.                     *
*************************************************************************/
