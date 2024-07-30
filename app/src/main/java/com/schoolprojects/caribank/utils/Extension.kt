package com.schoolprojects.caribank.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs


//toast function
fun Context.toast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


/*
@Composable
fun LoadingDialog(modifier: Modifier = Modifier) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))

    Box(
        modifier = Modifier
            .width(120.dp)
            .height(130.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.Transparent)
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever, modifier = Modifier.padding(4.dp)
        )
    }
}
*/


fun openWhatsapp(phoneNumber: String, context: Context) {

    val pm = context.packageManager
    val waIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber")
    )
    val info = pm.queryIntentActivities(waIntent, 0)
    if (info.isNotEmpty()) {
        context.startActivity(waIntent)
    } else {
        context.toast("WhatsApp not Installed")
    }
}

fun openDial(phoneNumber: String, context: Context) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phoneNumber")
    context.startActivity(intent)
}


//function to change milliseconds to date format
fun getDate(milliSeconds: Long?, dateFormat: String?): String {
    // Create a DateFormatter object for displaying date in specified format.
    val formatter = SimpleDateFormat(dateFormat)

    // Create a calendar object that will convert the date and time value in milliseconds to date.
    val calendar: Calendar? = Calendar.getInstance()
    calendar?.timeInMillis = milliSeconds!!
    return formatter.format(calendar?.time!!)
}

fun calculateDaysDifference(startDate: Calendar, endDate: Calendar): Int {
    val startTime = startDate.timeInMillis
    val endTime = endDate.timeInMillis
    val differenceInMillis = abs(endTime - startTime)
    val differenceInDays = (differenceInMillis / (1000 * 60 * 60 * 24)).toInt()
    return differenceInDays
}


 fun calculateInterest(amount: Double, withdrawDate: String): Double? {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    return try {
        val startDate = dateFormat.parse(today)
        val endDate = dateFormat.parse(withdrawDate)

        val diffInMillis = endDate.time - startDate.time
        val diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)

        amount + (amount * diffInDays * 0.01)
    } catch (e: Exception) {
        null
    }
}

 fun calculateInterestRate(amount: Double, withdrawDate: String): Double {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    return try {
        val startDate = dateFormat.parse(today)
        val endDate = dateFormat.parse(withdrawDate)

        val diffInMillis = endDate.time - startDate.time
        val diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)

        diffInDays * 1.0 // 1% per day
    } catch (e: Exception) {
        0.0
    }
}