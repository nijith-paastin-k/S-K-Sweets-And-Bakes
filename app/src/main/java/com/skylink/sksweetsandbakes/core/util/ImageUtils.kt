package com.skylink.sksweetsandbakes.core.util

import com.skylink.sksweetsandbakes.R

object ImageUtils {
    const val DRAWABLE_PREFIX = "android.resource://com.skylink.sksweetsandbakes/drawable/"

    fun getDrawableUri(drawableName: String): String {
        return "$DRAWABLE_PREFIX$drawableName"
    }

    val drawableMap = mapOf<String, Int>(
        "img_kaju_katli_pyramid" to R.drawable.img_kaju_katli_pyramid,
        "img_kaju_katli_brass_plate" to R.drawable.img_kaju_katli_brass_plate,
        "img_mysore_pak_brass_plate" to R.drawable.img_mysore_pak_brass_plate,
        "img_mysore_pak_namkeen_combo" to R.drawable.img_mysore_pak_namkeen_combo,
        "img_motichoor_ladoo" to R.drawable.img_motichoor_ladoo,
        "img_saffron_rasmalai" to R.drawable.img_saffron_rasmalai,
        "img_gulab_jamun_rasmalai_delight" to R.drawable.img_gulab_jamun_rasmalai_delight,
        "img_gulab_jamun_rasmalai_brass" to R.drawable.img_gulab_jamun_rasmalai_brass,
        "img_gulab_jamun_brass_bowl" to R.drawable.img_gulab_jamun_brass_bowl,
        "img_gulab_jamun_rasmalai_combo" to R.drawable.img_gulab_jamun_rasmalai_combo,
        "img_jalebi_brass_plate" to R.drawable.img_jalebi_brass_plate,
        "img_kalakand_ceramic_plate" to R.drawable.img_kalakand_ceramic_plate,
        "img_saffron_peda_platter" to R.drawable.img_saffron_peda_platter,
        "img_badam_halwa" to R.drawable.img_badam_halwa,
        "img_soan_papdi" to R.drawable.img_soan_papdi,
        "img_assorted_ghee_sweets" to R.drawable.img_assorted_ghee_sweets,
        "img_brass_bowl_sweets" to R.drawable.img_brass_bowl_sweets,
        "img_festive_brass_thali" to R.drawable.img_festive_brass_thali,
        "img_golden_samosas" to R.drawable.img_golden_samosas,
        "img_samosa_filter_coffee" to R.drawable.img_samosa_filter_coffee,
        "img_murukku_spiral_stack" to R.drawable.img_murukku_spiral_stack,
        "img_murukku_mixture_combo" to R.drawable.img_murukku_mixture_combo,
        "img_south_indian_mixture" to R.drawable.img_south_indian_mixture,
        "img_spicy_boondi_mixture" to R.drawable.img_spicy_boondi_mixture,
        "img_banana_chips_brass_bowl" to R.drawable.img_banana_chips_brass_bowl,
        "img_ribbon_pakoda" to R.drawable.img_ribbon_pakoda,
        "img_pepper_cashews" to R.drawable.img_pepper_cashews,
        "img_crispy_pastries_plate" to R.drawable.img_crispy_pastries_plate,
        "img_eggless_pastries" to R.drawable.img_eggless_pastries,
        "img_sweets_savory_triptych" to R.drawable.img_sweets_savory_triptych,
        "img_royal_mithai_gift_box" to R.drawable.img_royal_mithai_gift_box,
        "img_luxurious_sweets_box" to R.drawable.img_luxurious_sweets_box,
        "img_luxurious_sweets_box_alt" to R.drawable.img_luxurious_sweets_box_alt,
        "img_luxury_sweet_box" to R.drawable.img_luxury_sweet_box,
        "img_luxury_sweets_nuts_box" to R.drawable.img_luxury_sweets_nuts_box,
        "img_diwali_maroon_box_tray" to R.drawable.img_diwali_maroon_box_tray,
        "banner_diwali_diyas" to R.drawable.banner_diwali_diyas,
        "banner_festive_diwali_box" to R.drawable.banner_festive_diwali_box,
        "banner_festive_sweets_display" to R.drawable.banner_festive_sweets_display,
        "banner_festive_brass_platter" to R.drawable.banner_festive_brass_platter,
        "img_empty_search_plate" to R.drawable.img_empty_search_plate,
        "img_empty_wishlist_thali" to R.drawable.img_empty_wishlist_thali,
        "img_maroon_gift_scroll" to R.drawable.img_maroon_gift_scroll,
        "img_delivery_scooter" to R.drawable.img_delivery_scooter,
        "app_logo" to R.drawable.app_logo,
        "logo_sk_regal_lotus" to R.drawable.app_logo,
        "logo_sk_regal_lotus_alt" to R.drawable.app_logo,
        "logo_sk_kalash_gold" to R.drawable.app_logo,
        "logo_sk_lotus_gold" to R.drawable.app_logo,
        "logo_sk_ornate_emblem" to R.drawable.app_logo
    )

    /**
     * Resolves an image URL or resource URI to an appropriate model for Coil.
     */
    fun resolveModel(imageRef: Any?): Any {
        if (imageRef == null) return R.drawable.img_brass_bowl_sweets
        if (imageRef is Int) return imageRef
        val str = imageRef.toString().trim()
        if (str.isBlank()) return R.drawable.img_brass_bowl_sweets

        val cleanName = when {
            str.startsWith(DRAWABLE_PREFIX) -> str.removePrefix(DRAWABLE_PREFIX)
            str.startsWith("drawable/") -> str.removePrefix("drawable/")
            !str.startsWith("http://") && !str.startsWith("https://") && !str.startsWith("content://") -> str
            else -> null
        }

        if (cleanName != null) {
            val resId = drawableMap[cleanName]
            if (resId != null) return resId
        }

        return str
    }
}
